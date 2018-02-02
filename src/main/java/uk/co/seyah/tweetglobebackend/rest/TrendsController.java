package uk.co.seyah.tweetglobebackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.twitter.api.*;
import org.springframework.web.bind.annotation.*;
import uk.ac.soton.seyahml.SeyahML;
import uk.ac.soton.seyahml.api.SeyahMLAPI;
import uk.co.seyah.tweetglobebackend.model.graph.object.Score;
import uk.co.seyah.tweetglobebackend.model.user.User;
import uk.co.seyah.tweetglobebackend.service.IScoreRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/api/trends")
public class TrendsController {

    @Autowired
    private Twitter twitter;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getTrends() {
        Trends trends = twitter.searchOperations().getLocalTrends(44418);
        List<Trend> trendList = trends.getTrends().subList(0, 9);

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
        SearchParameters params = new SearchParameters(trend)
                .lang("en")
                .count(100)
                .resultType(SearchParameters.ResultType.RECENT)
                .includeEntities(false);
        List<Tweet> tweets = twitter.searchOperations().search(params).getTweets();
        List<HashMap<String, Object>> response = new ArrayList<>();
        for (Tweet tweet : tweets) {
            HashMap<String, Object> data = new HashMap<>();
            String text = tweet.getUnmodifiedText();
            String processedText = SeyahMLAPI.processTweetText(text);

            data.put("text", text);
            data.put("topic", SeyahMLAPI.classifyTopic(processedText));
            data.put("sentiment", SeyahMLAPI.classifySentiment(processedText));

            response.add(data);
        }
        return ResponseEntity.ok(response.toArray());
    }

}
