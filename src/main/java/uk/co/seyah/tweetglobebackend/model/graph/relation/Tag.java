package uk.co.seyah.tweetglobebackend.model.graph.relation;

import org.neo4j.ogm.annotation.*;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;

@RelationshipEntity(type = "Tag")
class Tag {

    @Id
    @GeneratedValue
    private Long graphId;

    @StartNode
    private Tweet tweet;

    @EndNode
    private Hashtag hashtag;

    public Tag() {
    }

    public Tag(Tweet tweet, Hashtag hashtag) {
        this.tweet = tweet;
        this.hashtag = hashtag;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public Hashtag getHashtag() {
        return hashtag;
    }

    public void setHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
    }
}
