package uk.co.seyah.tweetglobebackend.twitter;

import com.sun.istack.internal.logging.Logger;
import org.springframework.social.twitter.api.Tweet;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class TweetProcessor implements Runnable {

    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

    private final BlockingQueue<Tweet> queue;

    public TweetProcessor(BlockingQueue<Tweet> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Tweet tweet = queue.take();
                processTweet(tweet);
            } catch (InterruptedException e) {
                Logger.getLogger(this.getClass()).info("Shutting down Twitter stream service.");
            }
        }
    }

    private void processTweet(Tweet tweetEntity) {
        String lang = tweetEntity.getLanguageCode();
        String text = tweetEntity.getText();
        // filter non-English tweets:
        if (!"en".equals(lang)) {
            return;
        }

        System.out.println(text);
    }
}