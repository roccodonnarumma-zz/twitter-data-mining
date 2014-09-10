package com.project.services.movies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;
import com.project.model.movie.Movie;
import com.project.services.movies.impl.MovieServiceImpl;

public class MovieServiceTest {

    private static MovieServiceImpl movieService;
    private static ElasticsearchIndex index;

    @BeforeClass
    public static void setup() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Class<?> movieServiceClass = Class.forName("com.project.services.movies.impl.MovieServiceImpl");
        Constructor<?> constructor = movieServiceClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        movieService = (MovieServiceImpl)constructor.newInstance();

        Client client = NodeBuilder.nodeBuilder().node().client();

        index = new ElasticsearchIndex(client);
        movieService.setElasticsearchIndex(index);
    }

    @AfterClass
    public static void tearDown() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        index.closeClient();
    }

    @Test
    public void saveMovie() throws IOException, InterruptedException {
        Movie movie = new Movie();
        movie.setId("movie1");
        movieService.saveMovie(movie);

        Thread.sleep(1000);

        Movie result = movieService.getMovie("movie1");
        assertTrue("movie1".equals(result.getId()));
        index.bulkRemove(Type.MOVIE, Arrays.asList("movie1"));
    }

    @Test
    public void getHashTags() throws IOException, InterruptedException {
        Movie movie2 = new Movie();
        movie2.setId("movie2");
        movie2.setTracks("movie2Track");

        Movie movie3 = new Movie();
        movie3.setId("movie3");
        movie3.setTracks("movie3Track");

        movieService.saveMovie(movie2);
        movieService.saveMovie(movie3);

        Thread.sleep(1000);

        Map<String, String> hashtags = movieService.getHashtagMovies();
        assertTrue(hashtags.size() == 2);
        assertTrue(hashtags.containsKey("movie2Track"));
        assertTrue(hashtags.containsKey("movie3Track"));
        index.bulkRemove(Type.MOVIE, Arrays.asList("movie2", "movie3"));
    }

    @Test
    public void getMovies() throws IOException, InterruptedException {
        Movie movie1 = new Movie();
        movie1.setId("movie1");

        Movie movie2 = new Movie();
        movie2.setId("movie2");

        movieService.saveMovie(movie1);
        movieService.saveMovie(movie2);

        Thread.sleep(1000);

        List<Movie> movies = movieService.getMovies();
        assertTrue(movies.size() == 2);

        index.bulkRemove(Type.MOVIE, Arrays.asList("movie1", "movie2"));
    }

    @Test
    public void getTop10Movies() throws IOException, InterruptedException {
        List<String> ids = new LinkedList<>();
        for (int i = 0; i < 11; i++) {
            Movie movie = new Movie();
            String id = "movie" + i;
            movie.setId(id);
            movie.setTotalTweets(1);
            movie.setTotalSentiment(i);
            movieService.saveMovie(movie);
            ids.add(id);
        }

        Thread.sleep(1000);

        List<Movie> movies = movieService.getTop10Movies();
        assertTrue(movies.size() == 10);

        List<Integer> sentiments = new LinkedList<>();
        for (Movie movie : movies) {
            sentiments.add(movie.getTotalSentiment());
        }
        assertTrue(sentiments.size() == 10);
        assertFalse(sentiments.contains(0));

        index.bulkRemove(Type.MOVIE, ids);
    }
}
