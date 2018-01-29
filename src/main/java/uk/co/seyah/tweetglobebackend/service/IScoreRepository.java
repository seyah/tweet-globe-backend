package uk.co.seyah.tweetglobebackend.service;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import uk.co.seyah.tweetglobebackend.model.graph.object.Score;

import java.util.List;

public interface IScoreRepository extends Neo4jRepository<Score, String> {

    public List<Score> findAllByUser_Username(String username);

    public Score findByGraphId(Long graphId);

}
