package com.project.twitter.stream.config;

public class ClientConfig {

    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String tokenSecret;
    private final int queueCapacity;
    private final int numberOfThreads;

    public ClientConfig(String consumerKey, String consumerSecret, String token, String tokenSecret, int queueCapacity, int numberOfThreads) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.queueCapacity = queueCapacity;
        this.numberOfThreads = numberOfThreads;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }
}
