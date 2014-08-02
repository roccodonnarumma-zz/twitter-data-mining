package com.project.elasticsearch.type;

public enum Type {

    TWEET("tweet"),
    TRACK("track");

    private String name;

    private Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
