package uk.co.seyah.tweetglobebackend.service;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;

public interface ITweetRepository extends Neo4jRepository<Tweet, String> {

    public Tweet findOneByGraphId(Long graphId);

}
