package com.project.services.movies;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.project.model.movie.Movie;

/**
 * Service that groups common methods for movies.
 * 
 * @author rdonnarumma
 * 
 */
public interface MovieService {

    /**
     * Returns a Map where the key is the hashtag and the value is the movie identifier.
     * 
     * @return a Map where the key is the hashtag and the value is the movie identifier.
     */
    Map<String, String> getHashtagMovies();

    /**
     * Returns the movie for the given id.
     * 
     * @param id
     * @return the movie for the given id.
     * @throws IOException
     */
    Movie getMovie(String id) throws IOException;

    /**
     * Returns all the movies.
     * 
     * @return all the movies.
     * @throws IOException
     */
    List<Movie> getMovies() throws IOException;

    /**
     * Returns the top 10 movies with higher sentiment.
     * 
     * @return the top 10 movies with higher sentiment.
     * @throws IOException
     */
    List<Movie> getTop10Movies() throws IOException;

    /**
     * Saves the movie.
     * 
     * @param movie
     * @throws IOException
     */
    void saveMovie(Movie movie) throws IOException;
}
