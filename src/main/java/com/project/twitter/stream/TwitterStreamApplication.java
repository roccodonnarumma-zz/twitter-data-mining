package com.project.twitter.stream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.project.twitter.stream.client.TwitterStreamClient;
import com.project.twitter.stream.config.ClientConfig;
import com.project.twitter.stream.config.ConfigLoader;

@Configuration
@ComponentScan
public class TwitterStreamApplication {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(TwitterStreamApplication.class);

        ConfigLoader configLoader = context.getBean(ConfigLoader.class);
        ClientConfig config = configLoader.getClientConfig();

        TwitterStreamClient client = context.getBean(TwitterStreamClient.class);
        client.process(config);
    }
}
