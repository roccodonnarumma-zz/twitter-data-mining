package com.project.model.twitter;

import twitter4j.GeoLocation;

public class CustomGeoLocation extends GeoLocation {

    public CustomGeoLocation(double latitude, double longitude) {
        super(latitude, longitude);
    }

    @Override
    public String toString() {
        return "{" +
                "latitude=" + super.getLatitude() +
                ", longitude=" + super.getLongitude() +
                '}';
    }

}
