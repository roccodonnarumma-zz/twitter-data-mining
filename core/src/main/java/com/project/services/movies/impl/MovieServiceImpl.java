package com.project.services.movies.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.elasticsearch.field.MovieField;
import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;
import com.project.model.movie.Movie;
import com.project.services.movies.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

    private ElasticsearchIndex elasticsearchIndex;
    private ObjectMapper mapper;

    public MovieServiceImpl() {
        elasticsearchIndex = new ElasticsearchIndex();
        mapper = new ObjectMapper();
    }

    @Override
    public Map<String, String> getHashtagMovies() {
        Map<String, String> hashtagMovies = new HashMap<>();

        JSONArray array = elasticsearchIndex.getAll(Type.MOVIE);

        for (int i = 0; i < array.length(); i++) {
            JSONObject movieObject = array.getJSONObject(i);
            String tracks = movieObject.getString(MovieField.TRACKS.getName());
            for (String track : tracks.split(",")) {
                hashtagMovies.put(track, movieObject.getString(MovieField.ID.getName()));
            }
        }

        return hashtagMovies;
    }

    @Override
    public List<Movie> getMovies() throws IOException {
        JSONArray movies = elasticsearchIndex.getAll(Type.MOVIE);
        return mapper.readValue(movies.toString(), new TypeReference<List<Movie>>() {
        });
    }

    @Override
    public Movie getMovie(String id) throws IOException {
        JSONObject movieObject = elasticsearchIndex.get(Type.MOVIE, id);
        return mapper.readValue(movieObject.toString(), Movie.class);
    }

    @Override
    public void saveMovie(Movie movie) throws IOException {
        String json = mapper.writeValueAsString(movie);
        elasticsearchIndex.index(Type.MOVIE, movie.getId(), json);
    }
}
