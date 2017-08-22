package uk.co.seyah.tweetglobebackend.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;
import uk.co.seyah.tweetglobebackend.service.GraphService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class TwitterStreamIngester implements StreamListener {

    @Autowired
    private Twitter twitter;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private GraphService graphService;

    @Value("${taskExecutor.enabled}")
    private boolean isEnabled;

    private BlockingQueue<Tweet> queue = new ArrayBlockingQueue<>(15);

    public void run() {
        if(isEnabled) {
            List<StreamListener> listeners = new ArrayList<>();
            listeners.add(this);
            twitter.streamingOperations().sample(listeners);
        }
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        if (true) {
            for (int i = 0; i < taskExecutor.getMaxPoolSize(); i++) {
                taskExecutor.execute(new TweetProcessor(twitter, graphService, queue));
            }

            run();
        }
    }

    @Override
    public void onTweet(Tweet tweet) {
        queue.offer(tweet);
    }

    @Override
    public void onDelete(StreamDeleteEvent deleteEvent) {
    }

    @Override
    public void onLimit(int numberOfLimitedTweets) {
    }

    @Override
    public void onWarning(StreamWarningEvent warningEvent) {
    }
}