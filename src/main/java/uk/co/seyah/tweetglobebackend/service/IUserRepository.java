package uk.co.seyah.tweetglobebackend.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.seyah.tweetglobebackend.model.user.User;

@Repository
public interface IUserRepository extends MongoRepository<User, String> {

    public User findOneById(String id);

    public User findOneByEmail(String email);

    public User findOneByUsername(String username);

}
