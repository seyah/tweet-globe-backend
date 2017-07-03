package uk.co.seyah.tweetglobebackend.config;

import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

@Configuration
@EnableMongoHttpSession
public class SessionConfig {

        @Bean
        public MongoClientFactoryBean mongo() {
                MongoClientFactoryBean mongo = new MongoClientFactoryBean();
                mongo.setHost("localhost");
                return mongo;
        }

        @Bean
        public MongoTemplate mongoTemplate(Mongo mongo) {
                MongoTemplate template = new MongoTemplate(mongo, "mongoSession");
                return template;
        }
}