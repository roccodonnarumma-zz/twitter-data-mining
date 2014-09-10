package com.project.elasticsearch.field;

/**
 * Enum for Elasticsearch fields on Movie type.
 * 
 * @author rdonnarumma
 * 
 */
public enum MovieField {

    ID("id"),
    TITLE("title"),
    MPAA_RATING("mpaa_rating"),
    SYNOPSIS("synopsis"),
    RELEASE_DATES("release_dates"),
    POSTERS("posters"),
    ALTERNATE_IDS("alternate_ids"),
    TRACKS("tracks"),
    TOTAL_TWEETS("total_tweets"),
    TOTAL_SENTIMENT("total_sentiment");

    private String name;

    private MovieField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
