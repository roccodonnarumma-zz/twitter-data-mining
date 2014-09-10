package com.project.bolt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.twitter.CustomStatus;
import com.project.services.tweets.impl.TweetServiceImpl;

/**
 * ElasticSearch Bolt implementation that receives a tuple representing a CustomStatus and saves it in Elasticsearch.
 * 
 * @author rdonnarumma
 * 
 */
public class ElasticsearchBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 3361080905463165886L;

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchBolt.class);

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        TweetServiceImpl tweetService = TweetServiceImpl.getInstance();
        for (Object obj : input.getValues()) {
            if (obj instanceof String) {
                String statusString = (String)obj;
                try {
                    CustomStatus status = mapper.readValue(statusString, CustomStatus.class);
                    tweetService.saveTweet(status);
                } catch (IOException e) {
                    LOG.error(String.format("Error saving the status %s", statusString), e);
                }
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}
