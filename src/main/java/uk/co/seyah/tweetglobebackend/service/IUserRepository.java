package uk.co.seyah.tweetglobebackend.service;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import uk.co.seyah.tweetglobebackend.model.user.User;

public interface IUserRepository extends GraphRepository<User> {

    public User findOneByGraphId(Long graphId);

    public User findOneByEmail(String email);

    public User findOneByUsername(String username);

}
