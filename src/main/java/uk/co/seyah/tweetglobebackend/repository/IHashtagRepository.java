package uk.co.seyah.tweetglobebackend.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.relation.Connection;

import java.util.List;
import java.util.Map;

public interface IHashtagRepository extends Neo4jRepository<Hashtag, String> {

    Hashtag findOneByGraphId(Long graphId);

    List<Hashtag> findByWord(String word);
    Hashtag findOneByWord(String word);

    @Query("MATCH (h:Hashtag)<-[:Tag]-(t:Tweet) WITH h, COUNT(h) AS tagCount ORDER BY tagCount DESC LIMIT 10 RETURN  h.word, tagCount")
    List<Map> findTopTags();

    @Query("MATCH (h1:Hashtag)-[c:Connection]-(h2:Hashtag) WITH h1, c, h2 WHERE h1.word = {hashtag} RETURN h1, c, h2")
    List<Connection> findConnections(@Param("hashtag") String hashtag);

}
