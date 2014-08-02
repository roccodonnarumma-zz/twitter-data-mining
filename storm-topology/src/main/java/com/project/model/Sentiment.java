package com.project.model;

import java.io.Serializable;

import twitter4j.Status;

public class Sentiment implements Serializable {
    private static final long serialVersionUID = 3370802195227900772L;

    private Status status;
    private int sentiment;

    public Sentiment(Status status, int sentiment) {
        this.status = status;
        this.sentiment = sentiment;
    }

    public Status getStatus() {
        return status;
    }

    public int getSentiment() {
        return sentiment;
    }
}
