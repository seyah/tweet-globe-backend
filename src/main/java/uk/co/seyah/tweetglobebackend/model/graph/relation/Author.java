package uk.co.seyah.tweetglobebackend.model.graph.relation;


import org.neo4j.ogm.annotation.*;
import uk.co.seyah.tweetglobebackend.model.graph.object.Profile;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;

@RelationshipEntity(type = "Author")
public class Author {

    @GraphId
    private Long graphId;

    @StartNode
    private Tweet tweet;


    @EndNode
    private Profile profile;

    public Author() {
    }

    public Author(Tweet tweet, Profile profile) {
        this.tweet = tweet;
        this.profile = profile;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

}
