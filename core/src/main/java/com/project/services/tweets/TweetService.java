package com.project.services.tweets;

import com.project.model.sentiment.Sentiment;

public interface TweetService {

    void saveTweet(Sentiment sentiment);
}
