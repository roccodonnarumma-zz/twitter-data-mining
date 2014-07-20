package com.project.topology;

import java.io.IOException;
import java.util.Properties;

import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy.Units;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

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

        RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter("|");
        SyncPolicy syncPolicy = new CountSyncPolicy(10);
        FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(5.0f, Units.MB);
        FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath("/tweets/");

        HdfsBolt bolt = new HdfsBolt()
                .withFsUrl("hdfs://localhost:8020")
                .withFileNameFormat(fileNameFormat)
                .withRecordFormat(format)
                .withRotationPolicy(rotationPolicy)
                .withSyncPolicy(syncPolicy);

        builder.setBolt("hdfs", bolt).shuffleGrouping("twitter");

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(2);

        StormSubmitter.submitTopology("twitter-sentiment", conf, builder.createTopology());
        LOG.debug("Submitted topology twitter-sentiment");
    }
}
