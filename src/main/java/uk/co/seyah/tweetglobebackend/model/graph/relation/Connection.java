package uk.co.seyah.tweetglobebackend.model.graph.relation;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;

@RelationshipEntity(type = "Connection")
public class Connection {

    private Long graphId;

    @StartNode
    private Hashtag start;

    @EndNode
    private Hashtag end;

    private long count;

    public Connection() {
    }

    public Connection(Hashtag start, Hashtag end, long count) {
        this.start = start;
        this.end = end;
        this.count = count;
    }

    public void incrementConnection(int amt) {
        this.count += amt;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public Hashtag getStart() {
        return start;
    }

    public void setStart(Hashtag start) {
        this.start = start;
    }

    public Hashtag getEnd() {
        return end;
    }

    public void setEnd(Hashtag end) {
        this.end = end;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
