package com.project.services.tweets;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;
import com.project.model.twitter.CustomStatus;
import com.project.services.tweets.impl.TweetServiceImpl;

public class TwitterServiceTest {

    private static TweetServiceImpl tweetService;
    private static ElasticsearchIndex index;

    @BeforeClass
    public static void setup() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Class<?> tweetServiceClass = Class.forName("com.project.services.tweets.impl.TweetServiceImpl");
        Constructor<?> constructor = tweetServiceClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        tweetService = (TweetServiceImpl)constructor.newInstance();

        Client client = NodeBuilder.nodeBuilder().node().client();

        index = new ElasticsearchIndex(client);
        tweetService.setElasticsearchIndex(index);
    }

    @AfterClass
    public static void tearDown() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        index.closeClient();
    }

    @Test
    public void saveTweet() throws IOException, InterruptedException {
        CustomStatus status = new CustomStatus();
        status.setId(1);
        status.setCreatedAt(new Date());
        tweetService.saveTweet(status);

        Thread.sleep(1000);

        CustomStatus result = tweetService.getLatestTweet();
        assertTrue(result.getId() == 1);
        index.bulkRemove(Type.TWEET, Arrays.asList("1"));
    }

    @Test
    public void removeTweets() throws IOException, InterruptedException {
        CustomStatus status1 = new CustomStatus();
        status1.setId(1);
        status1.setCreatedAt(new Date());

        CustomStatus status2 = new CustomStatus();
        status2.setId(2);
        status2.setCreatedAt(new Date());

        CustomStatus status3 = new CustomStatus();
        status3.setId(3);
        status3.setCreatedAt(new Date());

        tweetService.saveTweet(status1);
        tweetService.saveTweet(status2);
        tweetService.saveTweet(status3);

        Thread.sleep(1000);

        tweetService.removeTweets(Arrays.asList("2", "3"));

        Thread.sleep(1000);

        CustomStatus result = tweetService.getLatestTweet();
        assertTrue(result.getId() == 1);

        index.bulkRemove(Type.TWEET, Arrays.asList("1"));
    }

    @Test
    public void getLatestTweet() throws IOException, InterruptedException {
        CustomStatus status1 = new CustomStatus();
        status1.setId(1);
        status1.setCreatedAt(new Date());
        status1.setMovieId("movie1");

        CustomStatus status2 = new CustomStatus();
        status2.setId(2);
        status2.setCreatedAt(new Date());
        status2.setMovieId("movie2");

        CustomStatus status3 = new CustomStatus();
        status3.setId(3);
        status3.setCreatedAt(new Date());
        status3.setMovieId("movie1");

        tweetService.saveTweet(status1);
        tweetService.saveTweet(status2);
        tweetService.saveTweet(status3);

        Thread.sleep(1000);

        CustomStatus result1 = tweetService.getLatestTweet("movie1");
        assertTrue(result1.getId() == 3);

        CustomStatus result2 = tweetService.getLatestTweet("movie2");
        assertTrue(result2.getId() == 2);

        index.bulkRemove(Type.TWEET, Arrays.asList("1", "2", "3"));
    }

    @Test
    public void countTweets() throws IOException, InterruptedException {
        CustomStatus status1 = new CustomStatus();
        status1.setId(1);
        status1.setCreatedAt(new Date());
        status1.setMovieId("movie1");

        CustomStatus status2 = new CustomStatus();
        status2.setId(2);
        status2.setCreatedAt(new Date());
        status2.setMovieId("movie2");

        CustomStatus status3 = new CustomStatus();
        status3.setId(3);
        status3.setCreatedAt(new Date());
        status3.setMovieId("movie1");

        tweetService.saveTweet(status1);
        tweetService.saveTweet(status2);
        tweetService.saveTweet(status3);

        Thread.sleep(1000);

        long result1 = tweetService.countTweets("movie1");
        assertTrue(result1 == 2);

        long result2 = tweetService.countTweets("movie2");
        assertTrue(result2 == 1);

        index.bulkRemove(Type.TWEET, Arrays.asList("1", "2", "3"));
    }
}
