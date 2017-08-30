package uk.co.seyah.tweetglobebackend.model.graph.object;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import uk.co.seyah.tweetglobebackend.model.graph.relation.Connection;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Hashtag {

    @GraphId
    private Long graphId;

    @Index(unique = true)
    private String word;

    private Long createdOn = new Date().getTime();

    @Relationship(type = "Connection", direction = Relationship.UNDIRECTED)
    private Set<Connection> connections;

    public Hashtag() {
    }

    public Hashtag(String word, Long createdOn) {
        this.word = word;
        this.createdOn = createdOn;
    }

    public void addConnection(Hashtag end) {
        if(this.connections == null) {
            connections = new HashSet<>();
        }
        for (Connection connection : connections){
            if(connection.getEnd().getGraphId().equals(end.getGraphId())) {
                connection.incrementConnection(1);
                return;
            }
        }
        Connection newConnection = new Connection(this, end, 1);
        connections.add(newConnection);
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

    public Set<Connection> getConnections() {
        return connections;
    }

    public void setConnections(Set<Connection> connections) {
        this.connections = connections;
    }
}
