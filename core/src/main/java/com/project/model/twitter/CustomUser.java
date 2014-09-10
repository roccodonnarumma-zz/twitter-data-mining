package com.project.model.twitter;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Custom User POJO.
 * 
 * @author rdonnarumma
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUser implements Serializable {
    private static final long serialVersionUID = 8025733536276840030L;

    private long id;
    private String name;

    @JsonProperty("screen_name")
    private String screenName;
    private String location;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    @JsonProperty("profile_image_url_https")
    private String profileImageUrlHttps;

    @JsonProperty("followers_count")
    private int followersCount;

    public CustomUser() {
    }

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
}
