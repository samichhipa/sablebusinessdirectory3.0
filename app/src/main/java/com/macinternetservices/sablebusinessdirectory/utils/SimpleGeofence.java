package com.macinternetservices.sablebusinessdirectory.utils;

import com.google.android.gms.location.Geofence;

public class SimpleGeofence {
    private final String id;
    private final double latitude;
    private final double longitude;
    private String isfeatured;
    private String city_id;
    private String item_name;
    private final float radius;
    private long expirationDuration;
    private int transitionType;
    private int loiteringDelay = 60000;

    public SimpleGeofence(String geofenceId, double latitude, double longitude,
                          float radius, long expiration, int transition,String isfeatured,String city_id,String item_name) {
        this.id = geofenceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.expirationDuration = expiration;
        this.transitionType = transition;
        this.isfeatured=isfeatured;
        this.city_id=city_id;
        this.item_name=item_name;
    }

    public Geofence toGeofence() {
        Geofence g = new Geofence.Builder()
                .setExpirationDuration(expirationDuration)
                .setRequestId(id)
                .setLoiteringDelay(loiteringDelay)
                .setTransitionTypes(transitionType)
                .setCircularRegion(latitude,longitude,radius)
                .build();
        return g;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public long getExpirationDuration() {
        return expirationDuration;
    }

    public void setExpirationDuration(long expirationDuration) {
        this.expirationDuration = expirationDuration;
    }

    public int getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(int transitionType) {
        this.transitionType = transitionType;
    }

    public int getLoiteringDelay() {
        return loiteringDelay;
    }

    public void setLoiteringDelay(int loiteringDelay) {
        this.loiteringDelay = loiteringDelay;
    }

    float getRadius() {
        return radius;
    }

    double getLongitude() {
        return longitude;
    }

    String getId() {
        return id;
    }

    double getLatitude() {
        return latitude;
    }


    public String getIsfeatured() {
        return isfeatured;
    }

    public void setIsfeatured(String isfeatured) {
        this.isfeatured = isfeatured;
    }
}