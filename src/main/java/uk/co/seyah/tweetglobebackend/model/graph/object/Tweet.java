package uk.co.seyah.tweetglobebackend.model.graph.object;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import uk.co.seyah.tweetglobebackend.model.graph.relation.Mention;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Tweet {

    @GraphId
    private Long graphId;

    @Relationship(type = "Author", direction = Relationship.UNDIRECTED)
    private Profile profile;

    @Relationship(type = "Tag")
    private Set<Hashtag> hashtags;

    @Relationship(type = "Mention")
    private Set<Profile> mentions;

    private String lang;
    private String text;
    private int favouriteCount;
    private int retweetCount;
    private boolean isRetweeted;
    private Date creationDate;

    public Tweet() {
    }

    public Tweet(org.springframework.social.twitter.api.Tweet tweetEntity) {
        this(tweetEntity.getLanguageCode(),
                tweetEntity.getText(),
                tweetEntity.getFavoriteCount(),
                tweetEntity.getRetweetCount(),
                tweetEntity.isRetweeted(),
                tweetEntity.getCreatedAt());
    }

    public Tweet(String lang, String text) {
        this.lang = lang;
        this.text = text;
    }

    public Tweet(String lang, String text, int favouriteCount, int retweetCount, boolean isRetweeted, Date creationDate) {
        this.lang = lang;
        this.text = text;
        this.favouriteCount = favouriteCount;
        this.retweetCount = retweetCount;
        this.isRetweeted = isRetweeted;
        this.creationDate = creationDate;
    }

    public void addHashtag(Hashtag hashtag) {
        if(hashtags == null) {
            hashtags = new HashSet<>();
        }
        hashtags.add(hashtag);
    }

    public void addMention(Profile profile) {
        if(mentions == null) {
            mentions = new HashSet<>();
        }
        mentions.add(profile);
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
