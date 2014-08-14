package com.project.model.twitter;

import java.io.Serializable;
import java.util.Date;

import twitter4j.GeoLocation;
import twitter4j.Status;

public class CustomStatus implements Serializable {

    private long id;
    private String movieId;
    private Date createdAt;
    private String text;
    private String source;
    private boolean favorited;
    private boolean retweeted;
    private int favoriteCount;
    private CustomGeoLocation geoLocation;
    private int retweetCount;
    private CustomUser user;

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
        GeoLocation geoLocation = status.getGeoLocation();
        if (geoLocation != null) {
            geoLocation = new CustomGeoLocation(geoLocation.getLatitude(), geoLocation.getLongitude());
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

    public String getMovieId() {
        return movieId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public String getSource() {
        return source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public CustomGeoLocation getGeoLocation() {
        return geoLocation;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public CustomUser getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", createdAt=" + createdAt +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", favorited=" + favorited +
                ", retweeted=" + retweeted +
                ", favoriteCount=" + favoriteCount +
                ", geoLocation=" + geoLocation +
                ", retweetCount=" + retweetCount +
                ", user=" + user +
                '}';
    }
}
