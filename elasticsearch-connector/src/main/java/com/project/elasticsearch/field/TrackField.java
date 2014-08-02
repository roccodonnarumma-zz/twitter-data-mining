package com.project.elasticsearch.field;

public enum TrackField {

    TRACKS("tracks");

    private String name;

    private TrackField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
