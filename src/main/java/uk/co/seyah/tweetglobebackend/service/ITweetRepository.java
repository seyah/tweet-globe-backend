package uk.co.seyah.tweetglobebackend.service;

import org.springframework.data.neo4j.repository.GraphRepository;
import uk.co.seyah.tweetglobebackend.model.graph.object.Profile;
import uk.co.seyah.tweetglobebackend.model.graph.object.Tweet;

public interface ITweetRepository extends GraphRepository<Tweet> {

    public Tweet findOneByGraphId(Long graphId);

    public Tweet findByProfile(Profile profile);

}
