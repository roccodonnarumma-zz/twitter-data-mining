package com.project.batch.reduce;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.project.batch.model.Tweet;
import com.project.model.movie.Movie;
import com.project.services.movies.MovieService;
import com.project.services.movies.impl.MovieServiceImpl;
import com.project.services.tweets.TweetService;
import com.project.services.tweets.impl.TweetServiceImpl;

/**
 * Reducer which receives all tweets for the same movie. It aggregates them and compute the total number of tweets and the total sentiment. Saves the results in
 * the index.
 * 
 * @author rdonnarumma
 * 
 */
public class MovieTweetReducer extends Reducer<Text, Tweet, NullWritable, NullWritable> {

    private MovieService movieService;
    private TweetService tweetService;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        movieService = MovieServiceImpl.getInstance();
        tweetService = TweetServiceImpl.getInstance();
    }

    /**
     * Reduce function that get as key the movie identifier and as value a sorted list (by tweet id) of all tweets for that movie. Calculates the total number
     * of tweets for that movie and the total sentiment and saves it in the index.
     * 
     * @param key
     * @param values
     * @param context
     */
    @Override
    public void reduce(Text key, Iterable<Tweet> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        int total = 0;

        List<String> ids = new LinkedList<>();

        for (Tweet tweet : values) {
            count++;
            total += tweet.getSentiment().get();
            ids.add(String.valueOf(tweet.getTweetId().get()));
        }

        tweetService.removeTweets(ids);

        Movie movie = movieService.getMovie(key.toString());
        movie.setTotalTweets(count);
        movie.setTotalSentiment(total);

        movieService.saveMovie(movie);
    }

}
