package uk.co.seyah.tweetglobebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.Profile;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GraphService {

    @Autowired
    private ITweetRepository tweetRepository;

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IHashtagRepository hashtagRepository;

    public Profile createOrAddProfile(Profile profile) {
        Profile p = profileRepository.findOneByName(profile.getName());
        if(p == null) {
            return profileRepository.save(profile);
        } else {
            return p;
        }
    }

    public Tweet addTweet(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    public Hashtag createOrAddHashtag(Hashtag hashtag) {
        Hashtag h = hashtagRepository.findOneByWord(hashtag.getWord());
        if(h == null) {
            return hashtagRepository.save(hashtag);
        } else {
            return h;
        }
    }

    public Tweet connectTweetWithAuthor(Tweet tweet, Profile author) {
        tweet.setProfile(author);
        return tweetRepository.save(tweet);
    }

    public Tweet connectTweetWithHashtag(Tweet tweet, Hashtag hashtag) {
        tweet.addHashtag(hashtag);
        return tweetRepository.save(tweet);
    }

    public Tweet connectTweetWithHashtags(Tweet tweet, Set<Hashtag> hashtags) {
        for (Hashtag hashtag : hashtags) {
            tweet.addHashtag(hashtag);
        }
        return tweetRepository.save(tweet);
    }

    public Tweet connectTweetWithMention(Tweet tweet, Profile mention) {
        tweet.addMention(mention);
        return tweetRepository.save(tweet);
    }

    public Tweet connectTweetWithMentions(Tweet tweet, Set<Profile> mentions) {
        for (Profile mention : mentions) {
            tweet.addMention(mention);
        }
        return tweetRepository.save(tweet);
    }

    public List<Map> findTopHashtags() {
        return hashtagRepository.findTopTags();
    }

}
