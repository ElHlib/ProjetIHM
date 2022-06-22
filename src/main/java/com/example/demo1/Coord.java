package com.example.demo1;

public class Coord {
    float latitude;
    float longitude;
    String geohash;
    int occurences;
    public Coord(float latitude,float longitude,String geohash,int occurences){
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
