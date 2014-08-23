package com.project.bolt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.project.model.twitter.CustomStatus;
import com.project.services.tweets.impl.TweetServiceImpl;

public class ElasticsearchBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 3361080905463165886L;

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        TweetServiceImpl tweetService = TweetServiceImpl.getInstance();
        for (Object obj : input.getValues()) {
            if (obj instanceof CustomStatus) {
                CustomStatus status = (CustomStatus)obj;
                try {
                    tweetService.saveTweet(status);
                } catch (IOException e) {
                    LOG.error(String.format("Error saving the status %s", status.getId()), e);
                }
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub
    }

}
