package com.project.elasticsearch.type;

public enum Type {

    TWEET("tweet"),
    MOVIE("movie");

    private String name;

    private Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
