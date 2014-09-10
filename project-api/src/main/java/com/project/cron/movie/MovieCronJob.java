package com.project.cron.movie;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.project.cron.converter.RottenTomatoesMessageConverter;
import com.project.model.movie.Movie;
import com.project.services.movies.MovieService;

/**
 * A Spring scheduled task implementation to get the movies that are on theaters from the Rotten Tomatoes API.
 * 
 * @author rdonnarumma
 * 
 */
@Component
public class MovieCronJob {

    public static final String BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0";
    public static final String IN_THEATERS_PATH = "/lists/movies/in_theaters.json";

    private String apiKey;
    private String pageLimit;
    private String country;

    @Autowired
    private MovieService movieService;

    /**
     * Construct a MovieCronJob loading the properties for the Rotten Tomatoes API from the properties files movie/rotten-tomatoes.properties.
     * 
     * @throws IOException
     */
    public MovieCronJob() throws IOException {
        Properties properties = new Properties();
        properties.load(MovieCronJob.class.getClassLoader().getResourceAsStream("movie/rotten-tomatoes.properties"));

        apiKey = properties.getProperty("rotten.tomatoes.apiKey");
        pageLimit = properties.getProperty("rotten.tomatoes.pageLimit");
        country = properties.getProperty("rotten.tomatoes.country");
    }

    /**
     * Init method that runs when the Spring context is loaded.
     * 
     * @throws IOException
     */
    public void init() throws IOException {
        fetchMoviesOnTheater();
    }

    /**
     * Runs every hour.
     * 
     * @throws IOException
     */
    @Scheduled(cron = "* 0 * * * ?")
    public void runEveryHour() throws IOException {
        fetchMoviesOnTheater();
    }

    /**
     * Fetches and saves all the movies that are currently on theaters using the Rotten Tomatoes API. If the movie is already saved, then it does not save it.
     * 
     * @throws IOException
     */
    private void fetchMoviesOnTheater() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new RottenTomatoesMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        int page = 1;
        int movies = 0;
        boolean fetch = true;

        while (fetch) {
            String url = BASE_URL + IN_THEATERS_PATH + "?apikey=" + apiKey + "&page=" + page + "&page_limit=" + pageLimit + "&country=" + country;
            MovieCronResult result = restTemplate.getForObject(url, MovieCronResult.class);
            for (Movie movie : result.getMovies()) {
                if ((movie.getPoster() != null) && (movie.getPoster() != null) && !movie.getPoster().getOriginal().contains("poster_default_thumb.gif")) {
                    Movie existingMovie = movieService.getMovie(movie.getId());
                    if ((existingMovie == null) || (existingMovie.getId() == null)) {
                        movieService.saveMovie(movie);
                    }
                }
            }

            movies += result.getMovies().size();
            page++;
            fetch = result.getTotal() > movies;
        }
    }

    public void setMovieService(MovieService movieService) {
        this.movieService = movieService;
    }
}
