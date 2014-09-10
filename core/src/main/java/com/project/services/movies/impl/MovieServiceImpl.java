package com.project.services.movies.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.elasticsearch.field.MovieField;
import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;
import com.project.model.movie.Movie;
import com.project.services.movies.MovieService;

/**
 * Implementation of the MovieService interface using the singleton pattern. The reason is to allow clients that cannot use Spring to get a singleton instance
 * of this service.
 * 
 * 
 * @author rdonnarumma
 * 
 */
@Service
public class MovieServiceImpl implements MovieService {

    private static MovieServiceImpl INSTANCE;

    @Autowired
    private ElasticsearchIndex elasticsearchIndex;

    private ObjectMapper mapper;

    private MovieServiceImpl() {
        mapper = new ObjectMapper();
    }

    public static MovieServiceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MovieServiceImpl();
            INSTANCE.setElasticsearchIndex(new ElasticsearchIndex());
        }
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.movies.MovieService#getHashtagMovies()
     */
    @Override
    public Map<String, String> getHashtagMovies() {
        Map<String, String> hashtagMovies = new HashMap<>();

        JSONArray array = elasticsearchIndex.search(Type.MOVIE, 1000, null, null, null);

        for (int i = 0; i < array.length(); i++) {
            JSONObject movieObject = array.getJSONObject(i);
            if (!movieObject.isNull(MovieField.TRACKS.getName())) {
                String tracks = movieObject.getString(MovieField.TRACKS.getName());
                for (String track : tracks.split(",")) {
                    hashtagMovies.put(track, movieObject.getString(MovieField.ID.getName()));
                }
            }
        }

        return hashtagMovies;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.movies.MovieService#getMovie(java.lang.String)
     */
    @Override
    public Movie getMovie(String id) throws IOException {
        JSONObject movieObject = elasticsearchIndex.get(Type.MOVIE, id);
        return mapper.readValue(movieObject.toString(), Movie.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.movies.MovieService#getMovies()
     */
    @Override
    public List<Movie> getMovies() throws IOException {
        JSONArray movies = elasticsearchIndex.search(Type.MOVIE, 1000, null, null, null);
        return mapper.readValue(movies.toString(), new TypeReference<List<Movie>>() {
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.movies.MovieService#getTop10Movies()
     */
    @Override
    public List<Movie> getTop10Movies() throws IOException {
        FilterBuilder filter = FilterBuilders.notFilter(FilterBuilders.termFilter(MovieField.TOTAL_TWEETS.getName(), 0));
        JSONArray movies = elasticsearchIndex.search(Type.MOVIE, 10, filter, MovieField.TOTAL_SENTIMENT.getName(), SortOrder.DESC);
        return mapper.readValue(movies.toString(), new TypeReference<List<Movie>>() {
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.project.services.movies.MovieService#saveMovie(com.project.model.movie.Movie)
     */
    @Override
    public void saveMovie(Movie movie) throws IOException {
        String json = mapper.writeValueAsString(movie);
        elasticsearchIndex.index(Type.MOVIE, movie.getId(), json);
    }

    public void setElasticsearchIndex(ElasticsearchIndex elasticsearchIndex) {
        this.elasticsearchIndex = elasticsearchIndex;
    }
}
