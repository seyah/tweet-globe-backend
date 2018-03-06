package uk.co.seyah.tweetglobebackend.model.graph.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.*;

@NodeEntity
class Score {

    @Id
    @GeneratedValue
    private Long graphId;

    private String label;
    private double score;

    @Relationship(type = "SCORER", direction = Relationship.UNDIRECTED)
    @JsonIgnore
    private User user;

    public Score() {
    }

    public Score(String label, double score) {
        this.label = label;
        this.score = score;
    }

    public Score(long graphId, String label, double score) {
        this.graphId = graphId;
        this.label = label;
        this.score = score;
    }

    public Score(String label, double score, User user) {
        this.label = label;
        this.score = score;
        this.user = user;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Score{" + "graphId=" + graphId + ", label='" + label + '\'' + ", score=" + score + ", user=" + user + '}';
    }
}
