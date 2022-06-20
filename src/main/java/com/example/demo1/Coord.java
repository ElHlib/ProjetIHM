package com.example.demo1;

public class Coord {
    float latitude;
    float longitude;
    String geohash;
    public Coord(float latitude,float longitude,String geohash){
        this.latitude=latitude;
        this.longitude=longitude;
        this.geohash=geohash;
    }
    void setLatLon(float latitude, float longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }
    void setGeohash(String geohash){
        this.geohash=geohash;
    }
}
