package uk.co.seyah.tweetglobebackend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.twitter.api.*;
import org.springframework.web.bind.annotation.*;
import uk.ac.soton.seyahml.SeyahML;
import uk.ac.soton.seyahml.api.SeyahMLAPI;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.User;
import uk.co.seyah.tweetglobebackend.model.graph.relation.UserBias;
import uk.co.seyah.tweetglobebackend.service.GraphService;
import uk.co.seyah.tweetglobebackend.repository.IUserRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommender")
public class RecommenderController {

    private final GraphService graphService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private Twitter twitter;

    @Autowired
    public RecommenderController(GraphService graphService) {
        this.graphService = graphService;
    }

    @RequestMapping(value = "/judgement", method = RequestMethod.PUT)
    public ResponseEntity<?> judgeTweet(@RequestBody HashMap<String, Object> response) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Set<Hashtag> hashtags = new HashSet<>();
        final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");
        Matcher matcher = HASHTAG_PATTERN.matcher(response.get("tweet").toString());
        while (matcher.find()) {
            String handle = matcher.group();
            Hashtag hashtag = new Hashtag(handle.substring(1), new Date().getTime());
            hashtag = graphService.createOrAddHashtag(hashtag);
            hashtags.add(hashtag);
        }

        graphService.connectHashtagsWithHashtags(hashtags);

        int decision = (int) response.get("judgement");
        String sentiment = (String) response.get("sentiment");
        int sentimentVal = sentiment.equalsIgnoreCase("positive") ? 1
                : sentiment.equalsIgnoreCase("negative") ? -1 : 0;

        for (Hashtag hashtag : hashtags) {
            Iterator<UserBias> iterator = user.getUserRatings().iterator();
            UserBias userBias = null;
            while (iterator.hasNext()) {
                UserBias ub = iterator.next();
                if (ub.getHashtag().getWord().equalsIgnoreCase(hashtag.getWord()) && ub.getSentiment() == sentimentVal) {
                    userBias = ub;
                    break;
                }
            }
            if (userBias == null) {
                userBias = new UserBias(user, hashtag, sentimentVal);
                user.getUserRatings().add(userBias);
            }
            if (decision == 1) {
                userBias.incrementYesCount();
            } else if (decision == -1) {
                userBias.incrementNoCount();
            } else {
                userBias.incrementCount();
            }
        }

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/training_tweet", method = RequestMethod.GET)
    public ResponseEntity<?> getTrainingTweet() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        List<UserBias> userBiases = userRepository.getUserBiases(user.getUsername());
        List<Hashtag> biases = userBiases.size() > 0 ?
                userBiases.subList(0, userBiases.size() >= 10 ? 10 : (userBiases.size() - 1)).stream()
                .map(UserBias::getHashtag).collect(Collectors.toList()) : new ArrayList<>();

        Trends trends = twitter.searchOperations().getLocalTrends(44418);
        List<Hashtag> hashtagList = new ArrayList<>();
        for (Trend trend : trends.getTrends().subList(0, trends.getTrends().size() < 10 ? trends.getTrends().size() : 10)) {
            Hashtag h = new Hashtag(trend.getName().replace("#", "").replace(" ", ""), new Date().getTime());
            h = graphService.createOrAddHashtag(h);
            hashtagList.add(h);
        }

        hashtagList.addAll(biases);
        StringBuilder sb = new StringBuilder();

        for (Hashtag hashtag : hashtagList) {
            sb.append("#").append(hashtag.getWord()).append(" OR ");
        }
        String query = sb.toString().substring(0, sb.length() - 4);

        SearchParameters params = new SearchParameters(query + " AND -filter:retweets AND -filter:replies")
                .lang("en")
                .count(100)
                .resultType(SearchParameters.ResultType.MIXED)
                .includeEntities(false);
        List<Tweet> tweets = twitter.searchOperations().search(params).getTweets();
        List<HashMap<String, Object>> tweetsDTO = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if(!tweet.isRetweet()) {
                tweetsDTO.add(tweetToHashMap(tweet));
            }
        }
        return ResponseEntity.ok(tweetsDTO.toArray());
    }

    @RequestMapping(value = "/recommendations", method = RequestMethod.GET)
    public ResponseEntity<?> getHashtagRecommendations(@RequestParam("recent") boolean isRecent) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        List<UserBias> userBiases = userRepository.getUserBiases(user.getUsername());
        List<Hashtag> biases = userBiases.size() > 0 ?
                userBiases.subList(0, userBiases.size() >= 8 ? 8 : (userBiases.size() - 1)).stream()
                        .map(UserBias::getHashtag).collect(Collectors.toList()) : new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (Hashtag hashtag : biases) {
            sb.append("#").append(hashtag.getWord()).append(" OR ");
        }
        String query = "(" + sb.toString().substring(0, sb.length() - 4) + ")";

        SearchParameters params = new SearchParameters(query + " AND -filter:retweets AND -filter:replies")
                .lang("en")
                .count(100)
                .resultType(isRecent ? SearchParameters.ResultType.RECENT : SearchParameters.ResultType.POPULAR)
                .includeEntities(false);
        List<Tweet> tweets = twitter.searchOperations().search(params).getTweets();
        List<HashMap<String, Object>> tweetsDTO = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if(!tweet.isRetweet()) {
                tweetsDTO.add(tweetToHashMap(tweet));
            }
        }
        return ResponseEntity.ok(tweetsDTO.toArray());
    }

    private HashMap<String, Object> tweetToHashMap(Tweet tweet) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("text", tweet.getUnmodifiedText());
        data.put("retweetCount", tweet.getRetweetCount());
        data.put("favoriteCount", tweet.getFavoriteCount());
        data.put("profileImage", tweet.getProfileImageUrl());
        data.put("user", tweet.getUser().getScreenName());
        String processedText = SeyahMLAPI.processTweetText(tweet.getUnmodifiedText());
        try {
            data.put("topic", SeyahML.getInstance().predictTopic(processedText).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            data.put("sentiment", SeyahML.getInstance().predictSentiment(processedText).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

}
