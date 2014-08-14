package com.project.model.twitter;

import java.io.Serializable;

public class CustomUser implements Serializable {

    private long id;
    private String name;
    private String screenName;
    private String location;
    private String profileImageUrl;
    private String profileImageUrlHttps;
    private int followersCount;

    public CustomUser(long id, String name, String screenName, String location, String profileImageUrl, String profileImageUrlHttps, int followersCount) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.location = location;
        this.profileImageUrl = profileImageUrl;
        this.profileImageUrlHttps = profileImageUrlHttps;
        this.followersCount = followersCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getLocation() {
        return location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", profileImageUrlHttps='" + profileImageUrlHttps + '\'' +
                ", followersCount=" + followersCount +
                '}';
    }
}
