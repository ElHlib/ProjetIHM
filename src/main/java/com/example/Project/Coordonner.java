package com.example.Project;

public class Coordonner {
    float latitude;
    float longitude;
    String geohash;
    int occurences;
    public Coordonner(float latitude, float longitude, String geohash, int occurences){
        this.latitude=latitude;
        this.longitude=longitude;
        this.geohash=geohash;
        this.occurences=occurences;
    }
    void setLatLon(float latitude, float longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }
    void setGeohash(String geohash){
        this.geohash=geohash;
    }
}
