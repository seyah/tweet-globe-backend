package uk.co.seyah.tweetglobebackend.service;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.Profile;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;

import java.util.List;
import java.util.Map;

public interface IHashtagRepository extends GraphRepository<Hashtag> {

    public Hashtag findOneByGraphId(Long graphId);

    public List<Hashtag> findByWord(String word);
    public Hashtag findOneByWord(String word);

    @Query("MATCH (h:Hashtag)<-[:Tag]-(t:Tweet) WITH h, COUNT(h) AS tagCount ORDER BY tagCount DESC LIMIT 10 RETURN  h.word, tagCount")
    List<Map> findTopTags();

}
