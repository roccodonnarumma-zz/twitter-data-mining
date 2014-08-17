package com.project.services.movies;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.project.model.movie.Movie;

public interface MovieService {

    Map<String, String> getHashtagMovies();

    List<Movie> getMovies() throws IOException;

    Movie getMovie(String id) throws IOException;

    void saveMovie(Movie movie) throws IOException;
}
