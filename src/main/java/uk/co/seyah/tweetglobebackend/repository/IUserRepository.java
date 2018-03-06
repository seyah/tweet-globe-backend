package uk.co.seyah.tweetglobebackend.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import uk.co.seyah.tweetglobebackend.model.graph.object.Hashtag;
import uk.co.seyah.tweetglobebackend.model.graph.object.User;
import uk.co.seyah.tweetglobebackend.model.graph.relation.UserBias;

import java.util.List;

public interface IUserRepository extends Neo4jRepository<User, String> {

    User findOneByGraphId(Long graphId);

    User findOneByEmail(String email);

    User findOneByUsername(String username);

    @Query("MATCH (u:User)-[b:UserBias]-(h:Hashtag) WITH u, b, h ORDER BY b.yesCount WHERE u.username = {username} RETURN u, b, h")
    List<UserBias> getUserBiases(@Param("username") String username);

    @Query("MATCH (u:User)-[b:UserBias]-(h:Hashtag) WITH u, b, h ORDER BY b.yesCount LIMIT {count} WHERE u.username = {username} RETURN u, b, h")
    List<Hashtag> getHashtagRecommendations(@Param("username") String username);

    @Query("MATCH (u:User)-[b:UserBias]-(h:Hashtag)-[c:Connection]-(h2:Hashtag) WHERE NOT (u)-[:UserBias]->(h2) WITH u, b, h, h2 ORDER BY b.yesCount WHERE u.username = {username} RETURN h2")
    List<Hashtag> getConnectionsForUser(@Param("username") String username);
}
