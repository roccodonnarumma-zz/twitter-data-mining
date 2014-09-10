package com.project.rest.controller.tweet;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.twitter.CustomStatus;
import com.project.services.tweets.TweetService;

/**
 * REST controller that contains all the methods that causes an read (GET) for tweets.
 * 
 * @author rdonnarumma
 * 
 */
@RestController
public class TweetQueriesController {

    @Autowired
    private TweetService tweetService;

    /**
     * GET API that returns the latest tweet as a JSON String.
     * 
     * @return the latest tweet as a JSON String.
     * @throws IOException
     */
    @RequestMapping(value = "/tweet/latest", method = RequestMethod.GET)
    public String getLatestTweet() throws IOException {
        CustomStatus status = tweetService.getLatestTweet();
        return buildJson(status);
    }

    /**
     * GET API that returns the latest tweet for the given movie identifier as a JSON String.
     * 
     * @param movieId
     * @return the latest tweet for the given movie identifier as a JSON String.
     * @throws IOException
     */
    @RequestMapping(value = "/tweet/latest/{movieId}", method = RequestMethod.GET)
    public String getLatestTweet(@PathVariable String movieId) throws IOException {
        CustomStatus status = tweetService.getLatestTweet(movieId);
        return buildJson(status);
    }

    /**
     * Returns the JSON representation String of the given Status.
     * 
     * @param status
     * @return the JSON representation String of the given Status.
     */
    private String buildJson(CustomStatus status) {
        if (status == null) {
            return "{}";
        }

        JSONObject object = new JSONObject();
        object.put("id", status.getId());
        object.put("user", status.getUser().getName());
        object.put("handle", status.getUser().getScreenName());
        object.put("thumbnail", status.getUser().getProfileImageUrl());
        object.put("date", status.getCreatedAt().getTime());
        object.put("tweet", status.getText());

        String score = "neutral";
        if (status.getSentiment() > 0) {
            score = "positive";
        } else if (status.getSentiment() < 0) {
            score = "negative";
        }

        object.put("score", score);

        return object.toString();
    }

}
