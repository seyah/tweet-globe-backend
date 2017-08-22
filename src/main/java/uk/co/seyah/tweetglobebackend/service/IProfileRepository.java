package uk.co.seyah.tweetglobebackend.service;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import uk.co.seyah.tweetglobebackend.model.graph.object.Profile;

import java.util.List;
import java.util.Map;

public interface IProfileRepository extends GraphRepository<Profile> {

    public Profile findOneByGraphId(Long graphId);

    public Profile findOneByName(String name);

    @Query("MATCH (p:Profile)<-[:Author]-(t:Tweet) WITH p, COUNT(p) AS mentionCount ORDER BY mentionCount DESC LIMIT 10 RETURN  p.screenName, mentionCount")
    List<Map> findTopTweeters();

    @Query("MATCH (p:Profile)<-[:Mention]-(t:Tweet) WITH p, COUNT(p) AS mentionCount ORDER BY mentionCount DESC LIMIT 10 RETURN  p.screenName, mentionCount")
    List<Map> findTopMentions();

}
