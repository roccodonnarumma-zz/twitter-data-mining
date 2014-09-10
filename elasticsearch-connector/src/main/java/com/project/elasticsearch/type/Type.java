package com.project.elasticsearch.type;

/**
 * Enum that contains the different Elasticsearch types in the index.
 * 
 * @author rdonnarumma
 * 
 */
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
