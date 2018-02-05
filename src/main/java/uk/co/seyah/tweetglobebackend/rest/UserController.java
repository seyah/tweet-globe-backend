package uk.co.seyah.tweetglobebackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.*;
import uk.ac.soton.seyahml.api.ISentiment;
import uk.ac.soton.seyahml.api.ITopic;
import uk.ac.soton.seyahml.api.SeyahMLAPI;
import uk.co.seyah.tweetglobebackend.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private Twitter twitter;

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @RequestMapping(value = "/{user}/analysis", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> analyseUser(@PathVariable String user) throws Exception {
        long searchCount = 1, lowestId = -1, iterations = 0;
        List<Tweet> tweets = new ArrayList<>();

        while (searchCount != 0 && iterations < 20) {
            List<Tweet> tweetList;
            if (lowestId == -1) {
                tweetList = twitter.timelineOperations().getUserTimeline(user, 100);
            } else {
                tweetList = twitter.timelineOperations().getUserTimeline(user, 100, 0, lowestId);
            }

            searchCount = tweetList.size();

            if (searchCount > 0) {
                lowestId = tweetList.get(tweetList.size() - 1).getId();
                tweets.addAll(tweetList);
            }

            iterations++;
        }

        List<String> texts = tweets.stream().map(tweet -> SeyahMLAPI.processTweetText(tweet.getUnmodifiedText()))
                .collect(Collectors.toList());
        List<ITopic> topics = SeyahMLAPI.classifyTopics(texts);
        List<ISentiment> sentiments = SeyahMLAPI.classifySentiments(texts);

        HashMap<String, Integer> sentimentCounter = new HashMap<>();
        for (ISentiment sentiment : sentiments) {
            if(sentimentCounter.containsKey(sentiment.getName())) {
                sentimentCounter.put(sentiment.getName(), sentimentCounter.get(sentiment.getName()) + 1);
            } else {
                sentimentCounter.put(sentiment.getName(), 1);
            }
        }

        HashMap<String, Integer> topicCounter = new HashMap<>();
        for (ITopic topic : topics) {
            if(topicCounter.containsKey(topic.getName())) {
                topicCounter.put(topic.getName(), topicCounter.get(topic.getName()) + 1);
            } else {
                topicCounter.put(topic.getName(), 1);
            }
        }

        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            ITopic topic = topics.get(i);
            ISentiment sentiment = sentiments.get(i);
        }

        return ResponseEntity.ok(new Object[]{sentimentCounter, topicCounter});
    }

}
