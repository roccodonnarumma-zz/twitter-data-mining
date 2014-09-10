package com.project.services.tweets;

import java.io.IOException;
import java.util.List;

import com.project.model.twitter.CustomStatus;

/**
 * Service that groups common methods for tweets.
 * 
 * @author rdonnarumma
 * 
 */
public interface TweetService {

    /**
     * Save the given tweet.
     * 
     * @param status
     * @throws IOException
     */
    void saveTweet(CustomStatus status) throws IOException;

    /**
     * Removes all tweets for the given ids.
     * 
     * @param ids
     */
    void removeTweets(List<String> ids);

    /**
     * Returns the latest created tweet.
     * 
     * @return the latest created tweet.
     * @throws IOException
     */
    CustomStatus getLatestTweet() throws IOException;

    /**
     * Returns the latest created tweet for the given movie identifier.
     * 
     * @param movieId
     * @return the latest created tweet for the given movie identifier.
     * @throws IOException
     */
    CustomStatus getLatestTweet(String movieId) throws IOException;

    /**
     * Returns the number of tweets for the given movie identifier.
     * 
     * @param movieId
     * @return the number of tweets for the given movie identifier.
     */
    long countTweets(String movieId);
}
