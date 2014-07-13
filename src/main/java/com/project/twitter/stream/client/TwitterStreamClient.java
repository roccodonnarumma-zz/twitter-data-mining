package com.project.twitter.stream.client;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twitter4j.StatusListener;

import com.google.common.collect.Lists;
import com.project.twitter.stream.config.ClientConfig;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;

@Component
public class TwitterStreamClient {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterStreamClient.class);

    @Autowired
    private List<? extends StatusListener> listeners;

    public void process(ClientConfig config) throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(config.getQueueCapacity());

        //TODO externalize this and reconnect when new track terms are added or removed
        List<String> trackTerms = Lists.newArrayList("movie");

        StatusesFilterEndpoint endPoint = new StatusesFilterEndpoint();
        endPoint.trackTerms(trackTerms);

        Authentication auth = new OAuth1(config.getConsumerKey(), config.getConsumerSecret(), config.getToken(), config.getTokenSecret());

        // Create a new BasicClient. By default gzip is enabled.
        BasicClient client = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .endpoint(endPoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue))
                .build();

        ExecutorService service = Executors.newFixedThreadPool(config.getNumberOfThreads());

        // Wrap our BasicClient with the twitter4j client
        Twitter4jStatusClient t4jClient = new Twitter4jStatusClient(client, queue, listeners, service);

        // Establish a connection
        t4jClient.connect();
        LOG.info("Connected to Twitter endPoint {}", endPoint.getURI());
        for (int threads = 0; threads < config.getNumberOfThreads(); threads++) {
            // This must be called once per processing thread
            t4jClient.process();
        }
    }
}
