package com.project.elasticsearch.field;

public enum TweetField {

    ID("id"),
    MOVIE_ID("movie_id"),
    MOVIE_NAME("movie_name"),
    SENTIMENT("sentiment"),
    TEXT("text"),
    CREATED_AT("created_at"),
    GEO_LOCATION("geo_location");

    private String name;

    private TweetField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
