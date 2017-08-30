package uk.co.seyah.tweetglobebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;
import uk.co.seyah.tweetglobebackend.model.graph.relation.Connection;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GraphService {

    @Autowired
    private ITweetRepository tweetRepository;

    @Autowired
    private IHashtagRepository hashtagRepository;

    public Tweet addTweet(Tweet tweet) {
        return tweetRepository.save(obfuscateTweet(tweet));
    }

    private Tweet obfuscateTweet(Tweet tweet) {
        String text = tweet.getText();
        text = text.replaceAll("@\\w+", "USERNAME");
        tweet.setText(text);
        return tweet;
    }

    public Hashtag createOrAddHashtag(Hashtag hashtag) {
        Hashtag h = hashtagRepository.findOneByWord(hashtag.getWord());
        if(h == null) {
            return hashtagRepository.save(hashtag);
        } else {
            return h;
        }
    }

    public Tweet connectTweetWithHashtag(Tweet tweet, Hashtag hashtag) {
        tweet.addHashtag(hashtag);
        return tweetRepository.save(tweet);
    }

    public void connectHashtagsWithHashtags(Set<Hashtag> ends) {
        for (Hashtag start : ends) {
            for(Hashtag end : ends) {
                if(!start.getGraphId().equals(end.getGraphId())) {
                    start.addConnection(end);
                }
            }
            hashtagRepository.save(start);
        }
    }

    public Tweet connectTweetWithHashtags(Tweet tweet, Set<Hashtag> hashtags) {
        for (Hashtag hashtag : hashtags) {
            tweet.addHashtag(hashtag);
        }
        return tweetRepository.save(tweet);
    }

    public List<Map> findTopHashtags() {
        return hashtagRepository.findTopTags();
    }

}
