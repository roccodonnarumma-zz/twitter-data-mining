package com.project.services.tweets.impl;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.elasticsearch.field.TweetField;
import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;
import com.project.model.twitter.CustomStatus;
import com.project.services.tweets.TweetService;

/**
 * Implementation of the TweetService interface using the singleton pattern. The reason is to allow clients that cannot use Spring to get a singleton instance
 * of this service.
 * 
 * @author rdonnarumma
 * 
 */
@Service
public class TweetServiceImpl implements TweetService {

    private static TweetServiceImpl INSTANCE;

    @Autowired
    private ElasticsearchIndex elasticsearchIndex;

    private ObjectMapper mapper;

    private TweetServiceImpl() {
        mapper = new ObjectMapper();
    }

    public static TweetServiceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TweetServiceImpl();
            INSTANCE.setElasticsearchIndex(new ElasticsearchIndex());
        }
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.tweets.TweetService#saveTweet(com.project.model.twitter.CustomStatus)
     */
    @Override
    public void saveTweet(CustomStatus status) throws IOException {
        String json = mapper.writeValueAsString(status);
        elasticsearchIndex.index(Type.TWEET, String.valueOf(status.getId()), json);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.tweets.TweetService#removeTweets(java.util.List)
     */
    @Override
    public void removeTweets(List<String> ids) {
        elasticsearchIndex.bulkRemove(Type.TWEET, ids);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.tweets.TweetService#getLatestTweet()
     */
    @Override
    public CustomStatus getLatestTweet() throws IOException {
        JSONArray results = elasticsearchIndex.search(Type.TWEET, 1, null, TweetField.CREATED_AT.getName(), SortOrder.DESC);
        if ((results != null) && (results.length() != 0)) {
            return mapper.readValue(results.get(0).toString(), CustomStatus.class);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.tweets.TweetService#getLatestTweet(java.lang.String)
     */
    @Override
    public CustomStatus getLatestTweet(String movieId) throws IOException {
        FilterBuilder filter = FilterBuilders.termFilter(TweetField.MOVIE_ID.getName(), movieId);
        JSONArray results = elasticsearchIndex.search(Type.TWEET, 1, filter, TweetField.CREATED_AT.getName(), SortOrder.DESC);
        if ((results != null) && (results.length() != 0)) {
            return mapper.readValue(results.get(0).toString(), CustomStatus.class);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.tweets.TweetService#countTweets(java.lang.String)
     */
    @Override
    public long countTweets(String movieId) {
        QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.termQuery(TweetField.MOVIE_ID.getName(), movieId));
        return elasticsearchIndex.count(Type.TWEET, query);
    }

    public void setElasticsearchIndex(ElasticsearchIndex elasticsearchIndex) {
        this.elasticsearchIndex = elasticsearchIndex;
    }

}
