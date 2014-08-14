package com.project.model.sentiment;

import java.io.Serializable;

import com.project.model.twitter.CustomStatus;

public class Sentiment implements Serializable {

    private CustomStatus status;
    private int sentiment;

    public Sentiment(CustomStatus status, int sentiment) {
        this.status = status;
        this.sentiment = sentiment;
    }

    public CustomStatus getStatus() {
        return status;
    }

    public int getSentiment() {
        return sentiment;
    }
}
