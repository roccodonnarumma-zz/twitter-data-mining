package com.project.rest.controller.movie;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.movie.Movie;
import com.project.services.movies.MovieService;

/**
 * REST controller that contains all the methods that causes an update (PUT, POST, DELETE) for movies.
 * 
 * @author rdonnarumma
 * 
 */
@RestController
public class MovieCommandsController {

    @Autowired
    private MovieService movieService;

    /**
     * POST API to add the given hashtags for the given movie.
     * 
     * @param id
     * @param hashtags
     * @throws IOException
     */
    @RequestMapping(value = "/movie/{id}", method = RequestMethod.POST)
    public void addHashTags(@PathVariable String id, @RequestBody String hashtags) throws IOException {
        Movie movie = movieService.getMovie(id);
        movie.setTracks(hashtags);
        movieService.saveMovie(movie);
    }
}
