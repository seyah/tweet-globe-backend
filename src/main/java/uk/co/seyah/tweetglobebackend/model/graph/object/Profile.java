package uk.co.seyah.tweetglobebackend.model.graph.object;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.social.twitter.api.TwitterProfile;

import java.util.Date;

@NodeEntity
public class Profile {

    @GraphId
    private Long graphId;

    @Index(unique = true)
    private String screenName;
    private String name;
    private String url;
    private String profileImageUrl;
    private String description;
    private String location;
    private Date createdDate;

    public Profile() {
    }

    public Profile(String screenName, String name, String url, String profileImageUrl, String description, String location, Date createdDate) {
        this.screenName = screenName;
        this.name = name;
        this.url = url;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.location = location;
        this.createdDate = createdDate;
    }

    public Profile(TwitterProfile profile) {
        this.screenName = profile.getScreenName();
        this.name = profile.getName();
        this.url = profile.getUrl();
        this.profileImageUrl = profile.getProfileImageUrl();
        this.description = profile.getDescription();
        this.location = profile.getLocation();
        this.createdDate = profile.getCreatedDate();
    }


    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
