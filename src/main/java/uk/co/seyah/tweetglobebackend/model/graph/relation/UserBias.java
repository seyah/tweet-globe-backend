package uk.co.seyah.tweetglobebackend.model.graph.relation;

import org.neo4j.ogm.annotation.*;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.User;

@RelationshipEntity(type = "UserBias")
public class UserBias {

    @Id
    @GeneratedValue
    private Long graphId;

    @StartNode
    private User user;

    @EndNode
    private Hashtag hashtag;

    @Property
    private Integer yesCount;

    @Property
    private Integer noCount;

    @Property
    private Integer totalCount;

    @Property
    private Integer sentiment;

    public UserBias() {
    }

    public UserBias(User user, Hashtag hashtag, int sentiment) {
        this.user = user;
        this.hashtag = hashtag;
        this.sentiment = sentiment;
        this.yesCount = 0;
        this.noCount = 0;
        this.totalCount = 0;
    }

    public void incrementYesCount() {
        this.yesCount++;
        this.totalCount++;
    }

    public void incrementNoCount() {
        this.noCount++;
        this.totalCount++;
    }

    public void incrementCount() {
        this.totalCount++;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public Hashtag getHashtag() {
        return hashtag;
    }

    public void setHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getYesCount() {
        return yesCount;
    }

    public void setYesCount(Integer yesCount) {
        this.yesCount = yesCount;
    }

    public Integer getNoCount() {
        return noCount;
    }

    public void setNoCount(Integer noCount) {
        this.noCount = noCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getSentiment() {
        return sentiment;
    }

    public void setSentiment(Integer sentiment) {
        this.sentiment = sentiment;
    }
}
