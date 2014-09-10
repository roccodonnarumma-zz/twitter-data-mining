package com.project.spout;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.FilterQuery;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import com.project.model.twitter.CustomStatus;
import com.project.services.movies.impl.MovieServiceImpl;

/**
 * Twitter Stream Spout implementation that opens a connection to Twitter Stream API and emits a CustomStatus.
 * 
 * @author rdonnarumma
 * 
 */
public class TwitterStreamSpout extends BaseRichSpout {
    private static final long serialVersionUID = 5833104464703359992L;

    private static final Logger LOG = LoggerFactory.getLogger(TwitterStreamSpout.class);

    private BlockingQueue<Status> queue;
    private SpoutOutputCollector collector;
    private TwitterStream twitterStream;
    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String tokenSecret;

    private Map<String, String> hashtagMovies;

    /**
     * Constructs a TwitterStreamSpout object with the given Twitter API parameters.
     * 
     * @param consumerKey
     * @param consumerSecret
     * @param token
     * @param tokenSecret
     */
    public TwitterStreamSpout(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see backtype.storm.spout.ISpout#open(java.util.Map, backtype.storm.task.TopologyContext, backtype.storm.spout.SpoutOutputCollector)
     * 
     * Init method of the Spout. It creates the queue and the Twitter Stream. It launches a new Thread that every 10 minutes will fetch all the tracks and
     * re-start filtering from Twitter's API.
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        queue = new LinkedBlockingQueue<>(1000);

        StatusListener listener = new StatusListener() {

            @Override
            public void onStatus(Status status) {
                queue.offer(status);
            }

            @Override
            public void onException(Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                LOG.warn("Track Limitation Notice: {}", numberOfLimitedStatuses);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                LOG.warn("Stall Warnin code: {} message: {}", warning.getCode(), warning.getMessage());
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                LOG.debug("ScrubGeo userId: {} upToStatusId: {}", userId, upToStatusId);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                LOG.warn("Deletion Notice userId: {} statusId: {}", statusDeletionNotice.getUserId(), statusDeletionNotice.getStatusId());
            }
        };

        twitterStream = new TwitterStreamFactory(new ConfigurationBuilder().setJSONStoreEnabled(true).build()).getInstance();
        twitterStream.addListener(listener);
        twitterStream.setOAuthConsumer(consumerKey, consumerSecret);
        twitterStream.setOAuthAccessToken(new AccessToken(token, tokenSecret));

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    MovieServiceImpl movieService = MovieServiceImpl.getInstance();
                    hashtagMovies = movieService.getHashtagMovies();
                    Set<String> tracksSet = hashtagMovies.keySet();
                    String[] tracks = new String[tracksSet.size()];

                    int count = 0;
                    for (String track : tracksSet) {
                        LOG.info("Using track: {}", track);
                        tracks[count] = track;
                        count++;
                    }

                    close();

                    FilterQuery query = new FilterQuery().track(tracks);
                    twitterStream.filter(query);
                    Utils.sleep(60000 * 10);
                }
            }
        });
        thread.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see backtype.storm.spout.ISpout#nextTuple()
     * 
     * Fetches a Status from the queue, gets the corresponding movie identifier and emits a CustomStatus.
     */
    @Override
    public void nextTuple() {
        Status status = queue.poll();
        if (status == null) {
            Utils.sleep(50);
        } else {
            for (HashtagEntity hashtagEntity : status.getHashtagEntities()) {
                String movieId = hashtagMovies.get(hashtagEntity.getText());
                if (movieId != null) {
                    CustomStatus customStatus = new CustomStatus(status, movieId);
                    collector.emit(new Values(customStatus));
                    break;
                }
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweet"));
    }

    @Override
    public void close() {
        if (twitterStream != null) {
            twitterStream.shutdown();
        }
    }
}
