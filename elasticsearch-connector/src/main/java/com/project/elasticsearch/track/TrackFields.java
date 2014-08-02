package com.project.elasticsearch.track;

public enum TrackFields {

    TRACKS("tracks");

    private String name;

    private TrackFields(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
