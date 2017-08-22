package uk.co.seyah.tweetglobebackend.service;

import org.springframework.data.neo4j.repository.GraphRepository;
import uk.co.seyah.tweetglobebackend.model.graph.object.Profile;

public interface IProfileRepository extends GraphRepository<Profile> {

    public Profile findOneByGraphId(Long graphId);

    public Profile findOneByName(String name);

}
