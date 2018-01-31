package uk.co.seyah.tweetglobebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import uk.ac.soton.seyahml.api.SeyahMLAPI;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;

@SpringBootApplication
public class TweetGlobeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TweetGlobeBackendApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws Exception {
        SeyahMLAPI.setupAPI(new File(this.getClass().getClassLoader().getResource("./model/topic/topic.model").toURI()),
                            new File(this.getClass().getClassLoader().getResource("./model/sentiment/sentiment.model")
                                             .toURI()));
    }
}
