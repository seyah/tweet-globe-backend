package uk.co.seyah.tweetglobebackend.model.graph.relation;

import org.neo4j.ogm.annotation.*;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;

@RelationshipEntity(type = "Connection")
public class Connection {

    @Id
    @GeneratedValue
    private Long graphId;

    @StartNode
    private Hashtag start;

    @EndNode
    private Hashtag end;

    @Property
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
