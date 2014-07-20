package com.project.topology;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

import com.project.bolt.HdfsBolt;
import com.project.spout.TwitterStreamSpout;

public class TwitterSentimentTopology {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterSentimentTopology.class);

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, IOException {
        Properties properties = new Properties();
        properties.load(TwitterSentimentTopology.class.getClassLoader().getResourceAsStream("twitter/config.properties"));

        //TODO externalize
        String[] tracks = new String[] { "movies" };

        LOG.debug("Using tracks: {}", tracks);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("twitter", new TwitterStreamSpout(properties.getProperty("twitter.consumerKey"),
                properties.getProperty("twitter.consumerSecret"),
                properties.getProperty("twitter.token"),
                properties.getProperty("twitter.tokenSecret"),
                tracks));

        builder.setBolt("hdfs", new HdfsBolt()).shuffleGrouping("twitter");

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(2);

        StormSubmitter.submitTopology("twitter-sentiment", conf, builder.createTopology());
        LOG.debug("Submitted topology twitter-sentiment");
    }
}
