package uk.co.seyah.tweetglobebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;
import uk.co.seyah.tweetglobebackend.model.graph.object.User;
import uk.co.seyah.tweetglobebackend.model.graph.relation.UserBias;
import uk.co.seyah.tweetglobebackend.repository.IHashtagRepository;
import uk.co.seyah.tweetglobebackend.repository.ITweetRepository;
import uk.co.seyah.tweetglobebackend.repository.IUserRepository;

import java.util.*;

@Service
public class GraphService {

    private final IUserRepository userRepository;

    @Autowired
    private ITweetRepository tweetRepository;

    @Autowired
    private IHashtagRepository hashtagRepository;

    @Autowired
    public GraphService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUserBias(User user, UserBias userBias) {
        if(user.getUserRatings() == null) {
            user.setUserRatings(new ArrayList<>());
        }
        user.getUserRatings().add(userBias);
        return userRepository.save(user);
    }

    public void addTweet(Tweet tweet) {
        tweetRepository.save(tweet);
    }

    public Hashtag createOrAddHashtag(Hashtag hashtag) {
        Hashtag h = hashtagRepository.findOneByWord(hashtag.getWord());
        if (h == null) {
            return hashtagRepository.save(hashtag);
        } else {
            h.setCount(h.getCount() + 1);
            hashtagRepository.save(h);
            return h;
        }
    }

    public void connectHashtagsWithHashtags(Set<Hashtag> ends) {
        Set<Hashtag> fill = new HashSet<>(ends);
        Iterator<Hashtag> iter = fill.iterator();
        while(iter.hasNext()) {
            Hashtag start = iter.next();
            start.setConnections(new HashSet<>(hashtagRepository.findConnections(start.getWord())));
            for (Hashtag end : fill) {
                if (!start.getWord().equals(end.getWord())) {
                    start.addConnection(end, true);
                }
            }
            iter.remove();
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
