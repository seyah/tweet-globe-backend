package uk.co.seyah.tweetglobebackend.model.graph.object;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import java.util.Date;

@NodeEntity
public class Hashtag {

    @GraphId
    private Long graphId;

    @Index(unique = true)
    private String word;

    private Long createdOn = new Date().getTime();

    public Hashtag() {
    }

    public Hashtag(String word, Long createdOn) {
        this.word = word;
        this.createdOn = createdOn;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }
}
