package com.project.batch.map;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.batch.model.Tweet;
import com.project.model.twitter.CustomStatus;

/**
 * Mapper which purpose is to generate key-value pairs of the type <movieId, Tweet>. The Tweet object contains the tweet identifier and the sentiment. MapReduce
 * will group all key-value pairs by key so each reducer will get all tweets for the same movie.
 * 
 * @author rdonnarumma
 * 
 */
public class MovieTweetMapper extends Mapper<LongWritable, Text, Text, Tweet> {

    private final Text outputKey = new Text();

    private ObjectMapper objectMapper;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        objectMapper = new ObjectMapper();
    }

    /**
     * Map function that get as key the line identifier and as value the line itself as text. It builds a Custom status object from the text and outputs the
     * movie identifier and a Tweet object containing the tweet identifier and the sentiment.
     * 
     * @param key
     * @param value
     * @param context
     */
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        CustomStatus status = objectMapper.readValue(line, CustomStatus.class);

        outputKey.set(status.getMovieId());

        context.write(outputKey, new Tweet(status.getId(), status.getSentiment()));
    }
}
