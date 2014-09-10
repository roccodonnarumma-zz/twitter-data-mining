package com.project.model.twitter;

import java.io.Serializable;
import java.util.Date;

import twitter4j.GeoLocation;
import twitter4j.Status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Custom Twitter Status POJO.
 * 
 * @author rdonnarumma
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomStatus implements Serializable {
    private static final long serialVersionUID = 8658287231627462851L;

    private long id;

    @JsonProperty("movie_id")
    private String movieId;

    @JsonProperty("created_at")
    private Date createdAt;
    private String text;
    private String source;
    private boolean favorited;
    private boolean retweeted;

    @JsonProperty("favorite_count")
    private int favoriteCount;

    @JsonProperty("geo_location")
    private CustomGeoLocation geoLocation;

    @JsonProperty("retweet_count")
    private int retweetCount;
    private CustomUser user;
    private int sentiment;

    public CustomStatus() {
    }

    public CustomStatus(Status status, String movieId) {
        id = status.getId();
        this.movieId = movieId;
        createdAt = status.getCreatedAt();
        text = status.getText();
        source = status.getSource();
        favorited = status.isFavorited();
        retweeted = status.isRetweeted();
        favoriteCount = status.getFavoriteCount();
        GeoLocation location = status.getGeoLocation();
        if (location != null) {
            geoLocation = new CustomGeoLocation(location.getLatitude(), location.getLongitude());
        }
        retweetCount = status.getRetweetCount();
        user = new CustomUser(status.getUser().getId(),
                status.getUser().getName(),
                status.getUser().getScreenName(),
                status.getUser().getLocation(),
                status.getUser().getProfileImageURL(),
                status.getUser().getProfileImageURLHttps(),
                status.getUser().getFollowersCount());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public CustomGeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(CustomGeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public int getSentiment() {
        return sentiment;
    }

    public void setSentiment(int sentiment) {
        this.sentiment = sentiment;
    }
}
