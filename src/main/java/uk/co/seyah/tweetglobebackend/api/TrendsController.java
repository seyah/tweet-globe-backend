package uk.co.seyah.tweetglobebackend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.*;
import org.springframework.web.bind.annotation.*;
import uk.ac.soton.seyahml.api.SeyahMLAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/api/trends")
public class TrendsController {

    private final Twitter twitter;

    @Autowired
    public TrendsController(Twitter twitter) {
        this.twitter = twitter;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getTrends() {
        Trends trends = twitter.searchOperations().getLocalTrends(44418);
        List<Trend> trendList = trends.getTrends();

        List<HashMap<String, String>> response = new ArrayList<>();
        for (Trend trend : trendList) {
            HashMap<String, String> data = new HashMap<>();
            data.put("name", trend.getName());
            data.put("query", trend.getQuery());
            response.add(data);
        }

        return ResponseEntity.ok(response.toArray());
    }

    @RequestMapping(value = "/{trend}", method = RequestMethod.GET)
    public ResponseEntity<?> getTrendData(@PathVariable String trend) throws Exception {
        long lowestId = -1, iterations = 0;
        List<Tweet> tweets = new ArrayList<>();

        while(tweets.size() < 500) {
            SearchParameters params;
            if(lowestId == -1) {
                params = new SearchParameters(trend + " AND -filter:retweets AND -filter:replies")
                        .lang("en")
                        .count(500)
                        .resultType(SearchParameters.ResultType.RECENT)
                        .includeEntities(false);
            } else {
                params = new SearchParameters(trend + " AND -filter:retweets AND -filter:replies")
                        .lang("en")
                        .count(500)
                        .resultType(SearchParameters.ResultType.RECENT)
                        .maxId(lowestId - 1)
                        .includeEntities(false);
            }
            tweets.addAll(twitter.searchOperations().search(params).getTweets());
            lowestId = tweets.get(tweets.size() - 1).getId();
            iterations++;
        }

        tweets = tweets.subList(0, 500);

        List<HashMap<String, Object>> response = new ArrayList<>();
        for (Tweet tweet : tweets) {
            HashMap<String, Object> data = new HashMap<>();
            String text = tweet.getUnmodifiedText();
            String processedText = SeyahMLAPI.processTweetText(text);

            data.put("text", text);
            data.put("user", tweet.getFromUser());
            data.put("topic", SeyahMLAPI.classifyTopic(processedText));
            data.put("sentiment", SeyahMLAPI.classifySentiment(processedText));
            data.put("date", tweet.getCreatedAt().getTime());

            response.add(data);
        }
        return ResponseEntity.ok(response.toArray());
    }

}
