package com.project.services.tweets;

import java.io.IOException;
import java.util.List;

import com.project.model.twitter.CustomStatus;

public interface TweetService {

    void saveTweet(CustomStatus status) throws IOException;

    void removeTweets(List<String> ids);

    CustomStatus getLatestTweet() throws IOException;

    CustomStatus getLatestTweet(String movieId) throws IOException;

    long countTweets(String movieId);
}
