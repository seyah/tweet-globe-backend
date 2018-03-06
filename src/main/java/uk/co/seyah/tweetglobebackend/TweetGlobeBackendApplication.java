package uk.co.seyah.tweetglobebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import uk.ac.soton.seyahml.api.SeyahMLAPI;

import java.io.File;
import java.util.Objects;

@SpringBootApplication
@EnableNeo4jRepositories("uk.co.seyah.tweetglobebackend.repository")
@EntityScan(basePackages = "uk.co.seyah.tweetglobebackend.model.graph")
public class TweetGlobeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TweetGlobeBackendApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws Exception {
        SeyahMLAPI.setupAPI(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("./model/topic/topic-NB.model")).toURI()),
                            new File(this.getClass().getClassLoader().getResource("./model/sentiment/sentiment-NB.model")
                                    .toURI()));
    }
}
