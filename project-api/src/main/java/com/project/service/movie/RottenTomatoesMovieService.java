package com.project.service.movie;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

public class RottenTomatoesMovieService {

    public static final String BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0";
    public static final String IN_THEATERS_PATH = "/lists/movies/in_theaters.json";

    private String apiKey;
    private String pageLimit;
    private String country;

    private Client client;

    public RottenTomatoesMovieService() throws IOException {
        Properties properties = new Properties();
        properties.load(RottenTomatoesMovieService.class.getClassLoader().getResourceAsStream("movie/rotten-tomatoes.properties"));

        client = Client.create();
        apiKey = properties.getProperty("rotten.tomatoes.apiKey");
        pageLimit = properties.getProperty("rotten.tomatoes.pageLimit");
        country = properties.getProperty("rotten.tomatoes.country");
    }

    public void getCurrentMovies() {
        WebResource resource = client
                .resource("?apikey=&page_limit=50&country=uk");
        String string = resource.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);

        JSONObject json = new JSONObject(string);
        System.out.println(json);
    }

}
