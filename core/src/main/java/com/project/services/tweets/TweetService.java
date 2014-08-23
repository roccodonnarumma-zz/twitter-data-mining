package com.project.services.tweets;

import java.io.IOException;

import com.project.model.twitter.CustomStatus;

public interface TweetService {

    void saveTweet(CustomStatus status) throws IOException;

    CustomStatus getLatestTweet() throws IOException;

    CustomStatus getLatestTweet(String movieId) throws IOException;
}
