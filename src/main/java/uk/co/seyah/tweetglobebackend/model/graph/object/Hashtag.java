package uk.co.seyah.tweetglobebackend.model.graph.object;

import org.neo4j.ogm.annotation.*;
import uk.co.seyah.tweetglobebackend.model.graph.relation.Connection;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Hashtag {

    @Id
    @GeneratedValue
    private Long graphId;

    @Index(unique = true)
    private String word;

    private Integer count;

    private Long createdOn = new Date().getTime();

    @Relationship(type = "Connection", direction = Relationship.UNDIRECTED)
    private Set<Connection> connections;

    public Hashtag() {
    }

    public Hashtag(String word, Long createdOn) {
        this.word = word;
        this.createdOn = createdOn;
        this.count = 1;
        this.connections = new HashSet<>();
    }

    public void addConnection(Hashtag end, boolean increment) {
        if(this.connections == null) {
            connections = new HashSet<>();
        }
        for (Connection connection : connections){
            if(connection.getEnd().getGraphId().equals(end.getGraphId())) {
                if(increment) {
                    connection.incrementConnection(1);
                }
                return;
            } else if (connection.getStart().getGraphId().equals(end.getGraphId())) {
                if(increment) {
                    connection.incrementConnection(1);
                }
                return;
            }
        }
        Connection newConnection = new Connection(this, end, 1);
        connections.add(newConnection);

    }

    private Long getGraphId() {
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
