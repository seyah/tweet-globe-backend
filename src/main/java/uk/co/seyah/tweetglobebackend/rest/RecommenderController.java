package uk.co.seyah.tweetglobebackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.soton.seyahml.SeyahML;
import uk.ac.soton.seyahml.api.SeyahMLAPI;
import uk.co.seyah.tweetglobebackend.model.graph.object.Score;
import uk.co.seyah.tweetglobebackend.model.user.User;
import uk.co.seyah.tweetglobebackend.service.IScoreRepository;
import uk.co.seyah.tweetglobebackend.service.IUserRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/api/recommender")
public class RecommenderController {

    @Autowired
    private IScoreRepository scoreRepository;

    @Autowired
    private Twitter twitter;

    @RequestMapping(value = "/scores", method = RequestMethod.GET)
    public ResponseEntity<?> getScores() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        List<Score> scores = new ArrayList<>(scoreRepository.findAllByUser_Username(user.getUsername()));

        return ResponseEntity.ok(scores.toArray());
    }

    @RequestMapping(value = "/scores", method = RequestMethod.PUT)
    public ResponseEntity<?> updateScore(@RequestBody Score score) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        score.setUser(user);
        scoreRepository.save(score);

        return getScores();
    }

    @RequestMapping(value = "/training_tweet", method = RequestMethod.GET)
    public ResponseEntity<?> getTrainingTweet() {
        SearchParameters params = new SearchParameters("#politics")
                .lang("en")
                .count(10)
                .resultType(SearchParameters.ResultType.MIXED)
                .includeEntities(false);
        List<Tweet> tweets = twitter.searchOperations().search(params).getTweets();
        List<HashMap<String, Object>> tweetsDTO = new ArrayList<>();
        for (Tweet tweet : tweets) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("text", tweet.getUnmodifiedText());
            data.put("retweetCount", tweet.getRetweetCount());
            data.put("favoriteCount", tweet.getFavoriteCount());
            data.put("profileImage", tweet.getProfileImageUrl());
            data.put("user", tweet.getUser().getScreenName());
            try {
                String processedText = SeyahMLAPI.processTweetText(tweet.getUnmodifiedText());
                data.put("topic", SeyahML.getInstance().predictTopic(processedText).getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            tweetsDTO.add(data);
        }
        return ResponseEntity.ok(tweetsDTO.toArray());
    }

}
