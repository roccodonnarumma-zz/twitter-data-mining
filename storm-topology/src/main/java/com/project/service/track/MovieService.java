package com.project.service.track;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.project.elasticsearch.field.MovieField;
import com.project.elasticsearch.index.ElasticsearchIndex;
import com.project.elasticsearch.type.Type;

public class MovieService {

    public static Map<String, String> getHashtagMovies() {
        Map<String, String> hashtagMovies = new HashMap<>();

        JSONArray array = ElasticsearchIndex.INSTANCE.getAll(Type.MOVIE);

        for (int i = 0; i < array.length(); i++) {
            JSONObject movieObject = array.getJSONObject(i);
            String tracks = movieObject.getString(MovieField.TRACKS.getName());
            for (String track : tracks.split(",")) {
                hashtagMovies.put(track, movieObject.getString(MovieField.MOVIE_ID.getName()));
            }
        }

        return hashtagMovies;
    }

    public static JSONArray getMovies() {
        return ElasticsearchIndex.INSTANCE.getAll(Type.MOVIE);
    }

}
