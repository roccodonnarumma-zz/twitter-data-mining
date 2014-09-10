package com.project.spout;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Status;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.tuple.Values;

import com.project.model.twitter.CustomStatus;

public class TwitterStreamSpoutTest {

    @Test
    public void twitterStreamSpout() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException, InvocationTargetException, JSONException {
        SpoutOutputCollector collector = mock(SpoutOutputCollector.class);

        TwitterStreamSpout spout = new TwitterStreamSpout(null, null, null, null);
        Field collectorField = spout.getClass().getDeclaredField("collector");
        collectorField.setAccessible(true);
        collectorField.set(spout, collector);

        Map<String, String> hashtagMovies = new HashMap<>();
        hashtagMovies.put("hashtag", "movie1");
        Field hashtagMoviesField = spout.getClass().getDeclaredField("hashtagMovies");
        hashtagMoviesField.setAccessible(true);
        hashtagMoviesField.set(spout, hashtagMovies);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 123);
        jsonObject.put("text", "this is a tweet");

        JSONObject userObject = new JSONObject();
        userObject.put("id", 11);

        jsonObject.put("user", userObject);

        JSONObject hashtagsObject = new JSONObject();
        hashtagsObject.put("indices", new JSONArray().put(0).put(0));
        hashtagsObject.put("text", "hashtag");

        JSONObject entitiesObject = new JSONObject();
        entitiesObject.put("hashtags", new JSONArray().put(hashtagsObject));

        jsonObject.put("entities", entitiesObject);

        Class<?> statusClass = Class.forName("twitter4j.StatusJSONImpl");
        Constructor<?> statusConstructor = statusClass.getDeclaredConstructor(JSONObject.class);
        statusConstructor.setAccessible(true);
        Status status = (Status)statusConstructor.newInstance(jsonObject);

        BlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);
        queue.add(status);
        Field queueField = spout.getClass().getDeclaredField("queue");
        queueField.setAccessible(true);
        queueField.set(spout, queue);

        spout.nextTuple();

        ArgumentCaptor<Values> emitValue = ArgumentCaptor.forClass(Values.class);
        verify(collector, atLeastOnce()).emit(emitValue.capture());

        CustomStatus result = (CustomStatus)emitValue.getValue().get(0);
        assertTrue(result.getId() == 123);
    }
}
