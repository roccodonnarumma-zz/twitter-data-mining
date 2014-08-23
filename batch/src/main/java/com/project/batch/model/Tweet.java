package com.project.batch.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.WritableComparable;

public class Tweet implements WritableComparable<Tweet> {

    private LongWritable tweetId;
    private IntWritable sentiment;

    public Tweet() {
        this(new LongWritable(), new IntWritable());
    }

    public Tweet(long tweetId, int sentiment) {
        this(new LongWritable(tweetId), new IntWritable(sentiment));
    }

    public Tweet(LongWritable tweetId, IntWritable sentiment) {
        this.tweetId = tweetId;
        this.sentiment = sentiment;
    }

    public LongWritable getTweetId() {
        return tweetId;
    }

    public IntWritable getSentiment() {
        return sentiment;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        tweetId.write(out);
        sentiment.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        tweetId.readFields(in);
        sentiment.readFields(in);
    }

    @Override
    public int compareTo(Tweet o) {
        return tweetId.compareTo(o.getTweetId());
    }

}
