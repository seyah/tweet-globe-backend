package uk.co.seyah.tweetglobebackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.SavedSearch;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/tweet")
public class TweetController {

    @Autowired
    private Twitter twitter;

    @RequestMapping(value = "/tweets", method = RequestMethod.GET)
    public ResponseEntity<?> getTweets() {
        SearchParameters params = new SearchParameters("#spring")
                .lang("en")
                .count(100)
                .resultType(SearchParameters.ResultType.RECENT)
                .includeEntities(false);
        List<Tweet> tweets = twitter.searchOperations().search(params).getTweets();

        return ResponseEntity.ok(tweets);
    }

}
