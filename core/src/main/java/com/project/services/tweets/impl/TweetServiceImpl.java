package com.project.services.tweets.impl;

import java.util.HashMap;
import java.util.Map;

import com.project.elasticsearch.field.TweetField;
import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;
import com.project.model.sentiment.Sentiment;
import com.project.model.twitter.CustomStatus;
import com.project.services.tweets.TweetService;

public class TweetServiceImpl implements TweetService {

    private ElasticsearchIndex elasticsearchIndex;

    public TweetServiceImpl() {
        elasticsearchIndex = new ElasticsearchIndex();
    }

    @Override
    public void saveTweet(Sentiment sentiment) {
        CustomStatus status = sentiment.getStatus();
        Map<String, Object> map = new HashMap<>();
        map.put(TweetField.MOVIE_ID.getName(), status.getMovieId());
        map.put(TweetField.SENTIMENT.getName(), sentiment.getSentiment());
        map.put(TweetField.TEXT.getName(), status.getText());
        map.put(TweetField.CREATED_AT.getName(), status.getCreatedAt());
        map.put(TweetField.GEO_LOCATION.getName(), status.getGeoLocation());
        elasticsearchIndex.index(Type.TWEET, String.valueOf(status.getId()), map);
    }
}
