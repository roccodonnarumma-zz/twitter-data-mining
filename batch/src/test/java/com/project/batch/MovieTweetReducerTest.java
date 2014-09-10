package com.project.batch;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.junit.Test;

import com.project.batch.model.Tweet;
import com.project.batch.reduce.MovieTweetReducer;
import com.project.model.movie.Movie;
import com.project.services.movies.impl.MovieServiceImpl;
import com.project.services.tweets.impl.TweetServiceImpl;

public class MovieTweetReducerTest {

    @Test
    public void reduce() throws IllegalArgumentException, IllegalAccessException, IOException, InterruptedException {
        TweetServiceImpl tweetService = mock(TweetServiceImpl.class);
        doNothing().when(tweetService).removeTweets(any(List.class));

        Movie movie = new Movie();
        MovieServiceImpl movieService = mock(MovieServiceImpl.class);
        when(movieService.getMovie(any(String.class))).thenReturn(movie);
        doNothing().when(movieService).saveMovie(any(Movie.class));

        MovieTweetReducer reducer = new MovieTweetReducer();

        Field[] fields = reducer.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ("tweetService".equals(field.getName())) {
                field.setAccessible(true);
                field.set(reducer, tweetService);
            }
            if ("movieService".equals(field.getName())) {
                field.setAccessible(true);
                field.set(reducer, movieService);
            }
        }

        reducer.reduce(new Text("movie1"), Arrays.asList(new Tweet(0, -2), new Tweet(0, 0), new Tweet(0, 1), new Tweet(0, 2)), null);
        assertTrue(movie.getTotalTweets() == 4);
        assertTrue(movie.getTotalSentiment() == 1);
    }
}
