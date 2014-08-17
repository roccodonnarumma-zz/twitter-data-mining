package com.project.rest.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.movie.Movie;
import com.project.services.movies.MovieService;

@RestController
public class MovieCommandsController {

    @Autowired
    private MovieService movieService;

    @RequestMapping(value = "/movie/{id}", method = RequestMethod.POST)
    public void addHashTags(@PathVariable String id, @RequestBody String hashtags) throws IOException {
        Movie movie = movieService.getMovie(id);
        movie.setTracks(hashtags);
        movieService.saveMovie(movie);
    }
}
