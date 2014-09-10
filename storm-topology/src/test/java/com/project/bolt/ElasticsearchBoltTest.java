package com.project.bolt;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import backtype.storm.tuple.Tuple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;
import com.project.model.twitter.CustomStatus;
import com.project.services.tweets.impl.TweetServiceImpl;

public class ElasticsearchBoltTest {

    private static TweetServiceImpl tweetService;
    private static ElasticsearchIndex index;

    @BeforeClass
    public static void setup() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        Class<?> tweetServiceClass = Class.forName("com.project.services.tweets.impl.TweetServiceImpl");
        Constructor<?> constructor = tweetServiceClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        tweetService = (TweetServiceImpl)constructor.newInstance();

        Field instance = tweetServiceClass.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(tweetService, tweetService);

        Client client = NodeBuilder.nodeBuilder().node().client();

        index = new ElasticsearchIndex(client);
        tweetService.setElasticsearchIndex(index);
    }

    @AfterClass
    public static void tearDown() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        index.closeClient();
    }

    @Test
    public void elasticsearchBolt() throws IOException, InterruptedException {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = this.getClass().getResourceAsStream("/twitter/status.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));

            ObjectMapper mapper = new ObjectMapper();
            String statusString = reader.readLine();
            CustomStatus status = mapper.readValue(statusString, CustomStatus.class);

            List<Object> values = new LinkedList<>();
            values.add(statusString);

            Tuple tuple = BoltTest.getTuple(values);

            ElasticsearchBolt bolt = new ElasticsearchBolt();
            bolt.execute(tuple, null);

            Thread.sleep(1000);

            CustomStatus result = tweetService.getLatestTweet();
            assertTrue(result.getId() == status.getId());

            index.bulkRemove(Type.TWEET, Arrays.asList("123"));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
        }

    }
}
