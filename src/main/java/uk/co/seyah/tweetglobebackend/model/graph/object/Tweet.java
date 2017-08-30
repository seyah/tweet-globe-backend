package uk.co.seyah.tweetglobebackend.model.graph.object;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Tweet {

    @GraphId
    private Long graphId;

    @Relationship(type = "Tag")
    private Set<Hashtag> hashtags;

    private String lang;
    private String text;
    private int favouriteCount;
    private int retweetCount;
    private boolean isRetweeted;
    private Long creationDate;
    private String location;

    public Tweet() {
    }

    public Tweet(org.springframework.social.twitter.api.Tweet tweetEntity) {
        this(tweetEntity.getLanguageCode(),
                tweetEntity.getText(),
                tweetEntity.getFavoriteCount(),
                tweetEntity.getRetweetCount(),
                tweetEntity.isRetweet(),
                tweetEntity.getCreatedAt().getTime(),
                tweetEntity.getUser().getLocation());
    }

    public Tweet(String lang, String text) {
        this.lang = lang;
        this.text = text;
    }

    public Tweet(String lang, String text, int favouriteCount, int retweetCount, boolean isRetweeted, Long creationDate, String location) {
        this.lang = lang;
        this.text = text;
        this.favouriteCount = favouriteCount;
        this.retweetCount = retweetCount;
        this.isRetweeted = isRetweeted;
        this.creationDate = creationDate;
        this.location = location;
    }

    public void addHashtag(Hashtag hashtag) {
        if(hashtags == null) {
            hashtags = new HashSet<>();
        }
        hashtags.add(hashtag);
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(int favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
