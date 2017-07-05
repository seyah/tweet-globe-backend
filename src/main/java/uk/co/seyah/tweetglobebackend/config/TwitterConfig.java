package uk.co.seyah.tweetglobebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfig {

    @Bean
    // Params injected from application.properties file:
    public Twitter twitter(final @Value("${social.twitter.consumerKey}") String appId,
                           final @Value("${social.twitter.consumerSecret}") String appSecret,
                           final @Value("${social.twitter.accessToken}") String accessToken,
                           final @Value("${social.twitter.accessTokenSecret}") String accessTokenSecret) {
        return new TwitterTemplate(appId, appSecret, accessToken, accessTokenSecret);
    }
}