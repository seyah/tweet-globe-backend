package uk.co.seyah.tweetglobebackend.twitter;

import com.sun.istack.internal.logging.Logger;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class TweetProcessor implements Runnable {

    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

    private final BlockingQueue<Tweet> queue;
    private Twitter twitter;

    public TweetProcessor(Twitter twitter, BlockingQueue<Tweet> queue) {
        this.twitter = twitter;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Tweet tweet = queue.take();
                processTweet(tweet);
            } catch (Exception e) {
                Logger.getLogger(this.getClass()).info("Shutting down Twitter processor.");
            }
        }
    }

    private void processTweet(Tweet tweetEntity) {
        String lang = tweetEntity.getLanguageCode();
        String text = tweetEntity.getUnmodifiedText();
        // filter non-English tweets:
        if (!"en".equals(lang)) {
            return;
        }

        System.out.println(text);
    }
}