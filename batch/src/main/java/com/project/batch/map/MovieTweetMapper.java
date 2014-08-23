package com.project.batch.map;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.batch.model.Tweet;
import com.project.model.twitter.CustomStatus;

public class MovieTweetMapper extends Mapper<LongWritable, Text, Text, Tweet> {

    private final Text outputKey = new Text();

    private ObjectMapper objectMapper;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        CustomStatus status = objectMapper.readValue(line, CustomStatus.class);

        outputKey.set(status.getMovieId());

        context.write(outputKey, new Tweet(status.getId(), status.getSentiment()));
    }
}
