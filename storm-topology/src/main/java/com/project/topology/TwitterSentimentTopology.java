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

import com.project.bolt.ElasticsearchBolt;
import com.project.bolt.SentimentBolt;
import com.project.spout.TwitterStreamSpout;

/**
 * Main class that creates a Storm Topology and submit it to the cluster.
 * 
 * @author rdonnarumma
 * 
 */
public class TwitterSentimentTopology {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterSentimentTopology.class);

    /**
     * Main method that builds a Topology using the properties in twitter/config.properties.
     * 
     * @param args
     * @throws AlreadyAliveException
     * @throws InvalidTopologyException
     * @throws IOException
     */
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, IOException {
        Properties properties = new Properties();
        properties.load(TwitterSentimentTopology.class.getClassLoader().getResourceAsStream("twitter/config.properties"));

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("twitter", new TwitterStreamSpout(properties.getProperty("twitter.consumerKey"),
                properties.getProperty("twitter.consumerSecret"),
                properties.getProperty("twitter.token"),
                properties.getProperty("twitter.tokenSecret")));

        RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter(properties.getProperty("hdfs.field.delimiter"));
        SyncPolicy syncPolicy = new CountSyncPolicy(10);
        FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(Long.parseLong(properties.getProperty("hdfs.file.size")), Units.MB);
        FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath(properties.getProperty("hdfs.folder.name"));

        HdfsBolt bolt = new HdfsBolt()
                .withFsUrl("hdfs://" + properties.getProperty("hdfs.address"))
                .withFileNameFormat(fileNameFormat)
                .withRecordFormat(format)
                .withRotationPolicy(rotationPolicy)
                .withSyncPolicy(syncPolicy);

        builder.setBolt("sentiment", new SentimentBolt()).shuffleGrouping("twitter");
        builder.setBolt("hdfs", bolt).shuffleGrouping("sentiment");
        builder.setBolt("index", new ElasticsearchBolt()).shuffleGrouping("sentiment");

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(2);

        StormSubmitter.submitTopology("twitter-sentiment", conf, builder.createTopology());
        LOG.debug("Submitted topology twitter-sentiment");
    }
}
