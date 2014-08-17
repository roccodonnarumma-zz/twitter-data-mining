package com.project.cron.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.model.movie.Movie;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieCronResult {

    private int total;

    private List<Movie> movies;

    public int getTotal() {
        return total;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
