package com.project.twitter.stream.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/twitter/stream/stream.properties")
public class ConfigLoader {

    @Autowired
    Environment env;

    @Bean
    public ClientConfig getClientConfig() {
        String consumerKey = env.getProperty("twitter.stream.consumer.key");
        String consumerSecret = env.getProperty("twitter.stream.consumer.secret");
        String token = env.getProperty("twitter.stream.token");
        String tokenSecret = env.getProperty("twitter.stream.token.secret");
        int queueCapacity = Integer.valueOf(env.getProperty("twitter.stream.queue.capacity"));
        int numberOfThreads = Integer.valueOf(env.getProperty("twitter.stream.process.threads"));
        return new ClientConfig(consumerKey, consumerSecret, token, tokenSecret, queueCapacity, numberOfThreads);
    }

}
