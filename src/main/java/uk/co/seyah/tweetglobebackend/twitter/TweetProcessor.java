package uk.co.seyah.tweetglobebackend.twitter;

import com.sun.istack.internal.logging.Logger;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.Profile;
import uk.co.seyah.tweetglobebackend.service.GraphService;
import uk.co.seyah.tweetglobebackend.service.ITweetRepository;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetProcessor implements Runnable {


    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");
    private static final Pattern MENTION_PATTERN = Pattern.compile("@\\w+");

    private Twitter twitter;
    private GraphService graphService;
    private final BlockingQueue<Tweet> queue;

    public TweetProcessor(Twitter twitter, GraphService graphService, BlockingQueue<Tweet> queue) {
        this.twitter = twitter;
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
        if (!"en".equals(lang)) {
            return;
        }

        uk.co.seyah.tweetglobebackend.model.graph.object.Tweet tweet = new uk.co.seyah.tweetglobebackend.model.graph.object.Tweet(tweetEntity);
        Profile author = new Profile(tweetEntity.getUser());



        graphService.addTweet(tweet);
        author = graphService.createOrAddProfile(author);

        graphService.connectTweetWithAuthor(tweet, author);
        graphService.connectTweetWithHashtags(tweet, hashtagsFromText(tweet.getText()));
        graphService.connectTweetWithMentions(tweet, mentionsFromText(tweet.getText()));
    }

    private Set<Hashtag> hashtagsFromText(String text) {
        Set<Hashtag> hashtags = new HashSet<>();
        Matcher matcher = HASHTAG_PATTERN.matcher(text);
        while (matcher.find()) {
            String handle = matcher.group();
            // removing '#' prefix
            Hashtag hashtag = new Hashtag(handle.substring(1), new Date());
            hashtag = graphService.createOrAddHashtag(hashtag);
            hashtags.add(hashtag);
        }
        return hashtags;
    }

    private Set<Profile> mentionsFromText(String text) {
        Set<Profile> mentions = new HashSet<>();
        Matcher matcher = MENTION_PATTERN.matcher(text);
        while (matcher.find()) {
            String handle = matcher.group();
            TwitterProfile twitterProfile = twitter.userOperations().getUserProfile(handle.substring(1));
            if(twitterProfile != null){
                Profile profile = new Profile(twitterProfile);
                profile = graphService.createOrAddProfile(profile);
                mentions.add(profile);
            }
        }
        return mentions;
    }
}