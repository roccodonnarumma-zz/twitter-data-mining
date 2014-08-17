package com.project.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.project.model.sentiment.Sentiment;
import com.project.services.tweets.TweetService;
import com.project.services.tweets.impl.TweetServiceImpl;

public class ElasticsearchBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 3361080905463165886L;

    private TweetService tweetService;

    public ElasticsearchBolt() {
        tweetService = new TweetServiceImpl();
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        for (Object obj : input.getValues()) {
            if (obj instanceof Sentiment) {
                Sentiment sentiment = (Sentiment)obj;
                tweetService.saveTweet(sentiment);
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub
    }

}
