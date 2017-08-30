package uk.co.seyah.tweetglobebackend.twitter;

import com.sun.istack.internal.logging.Logger;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.service.GraphService;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetProcessor implements Runnable {

    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

    private GraphService graphService;
    private final BlockingQueue<Tweet> queue;

    public TweetProcessor(GraphService graphService, BlockingQueue<Tweet> queue) {
        this.graphService = graphService;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Tweet tweet = queue.take();
                processTweet(tweet);
            } catch (Exception e) {
                Logger.getLogger(this.getClass()).warning("Shutting down Twitter processor. " + e.getMessage());
            }
        }
    }

    private void processTweet(Tweet tweetEntity) {
        String lang = tweetEntity.getLanguageCode();
        // filter non-English tweets:
        if (!"en".equals(lang) || tweetEntity.isRetweet()) {
            return;
        }

        uk.co.seyah.tweetglobebackend.model.graph.object.Tweet tweet = new uk.co.seyah.tweetglobebackend.model.graph.object.Tweet(tweetEntity);


        graphService.addTweet(tweet);
        graphService.connectTweetWithHashtags(tweet, hashtagsFromText(tweet.getText()));
    }

    private Set<Hashtag> hashtagsFromText(String text) {
        Set<Hashtag> hashtags = new HashSet<>();
        Matcher matcher = HASHTAG_PATTERN.matcher(text);
        while (matcher.find()) {
            String handle = matcher.group();
            // removing '#' prefix
            Hashtag hashtag = new Hashtag(handle.substring(1), new Date().getTime());
            hashtag = graphService.createOrAddHashtag(hashtag);
            hashtags.add(hashtag);
        }
        return hashtags;
    }
}