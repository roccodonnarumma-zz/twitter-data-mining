package com.project.rest.controller.movie;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.movie.Movie;
import com.project.services.movies.MovieService;
import com.project.services.tweets.TweetService;

/**
 * REST controller that contains all the methods that causes an read (GET) for movies.
 * 
 * @author rdonnarumma
 * 
 */
@RestController
public class MovieQueriesController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private TweetService tweetService;

    /**
     * GET API that returns all the movies as a JSON array.
     * 
     * @return all the movies as a JSON array.
     * @throws IOException
     */
    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public String getMovies() throws IOException {
        List<Movie> movies = movieService.getMovies();

        JSONArray results = new JSONArray();
        for (Movie movie : movies) {
            JSONObject object = new JSONObject();
            object.put("id", movie.getId());
            object.put("name", movie.getTitle());
            if (movie.getPoster() != null) {
                object.put("thumbnail", movie.getPoster().getOriginal());
            }
            object.put("tracks", movie.getTracks());
            results.put(object);
        }

        return results.toString();
    }

    /**
     * GET API that returns the movie for the given id as a JSON String.
     * 
     * @param id
     * @return the movie for the given id as a JSON String.
     * @throws IOException
     */
    @RequestMapping(value = "/movie/{id}", method = RequestMethod.GET)
    public String getMovie(@PathVariable String id) throws IOException {
        Movie movie = movieService.getMovie(id);

        JSONObject object = new JSONObject();
        object.put("id", movie.getId());
        object.put("name", movie.getTitle());
        object.put("description", movie.getSynopsis());
        if (movie.getPoster() != null) {
            object.put("thumbnail", movie.getPoster().getOriginal());
        }
        if (movie.getReleaseDate() != null) {
            object.put("releaseDate", movie.getReleaseDate().getTheater());
        }
        object.put("rating", movie.getMpaaRating());
        if (movie.getAlternateId() != null) {
            object.put("imdb", movie.getAlternateId().getImdb());
        }

        long totalTweets = tweetService.countTweets(movie.getId());

        object.put("numberOfTweets", totalTweets + movie.getTotalTweets());

        String score = "positive";
        if (movie.getTotalSentiment() > 0) {
            score = "positive";
        } else if (movie.getTotalSentiment() < 0) {
            score = "negative";
        }
        object.put("score", score);

        return object.toString();
    }

    /**
     * GET API that returns the list of top 10 movies as a JSON array.
     * 
     * @return the list of top 10 movies as a JSON array.
     * @throws IOException
     */
    @RequestMapping(value = "/movie/top10", method = RequestMethod.GET)
    public String getTop10() throws IOException {
        List<Movie> movies = movieService.getTop10Movies();

        JSONArray results = new JSONArray();
        for (Movie movie : movies) {
            JSONObject object = new JSONObject();
            object.put("id", movie.getId());
            object.put("name", movie.getTitle());
            object.put("thumbnail", movie.getPoster().getOriginal());
            object.put("totalSentiment", movie.getTotalSentiment());
            object.put("totalTweets", movie.getTotalTweets());
            results.put(object);
        }
        return results.toString();
    }
}
