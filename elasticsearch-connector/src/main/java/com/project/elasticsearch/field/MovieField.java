package com.project.elasticsearch.field;

public enum MovieField {

    MOVIE_ID("movieId"),
    TRACKS("tracks");

    private String name;

    private MovieField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
