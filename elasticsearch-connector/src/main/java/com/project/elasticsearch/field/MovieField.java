package com.project.elasticsearch.field;

public enum MovieField {

    ID("id"),
    TITLE("title"),
    MPAA_RATING("mpaa_rating"),
    SYNOPSIS("synopsis"),
    RELEASE_DATES("release_dates"),
    POSTERS("posters"),
    ALTERNATE_IDS("alternate_ids"),
    TRACKS("tracks");

    private String name;

    private MovieField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
