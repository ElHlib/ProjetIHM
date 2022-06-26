package com.Project;

public class Coordonnees {
    float latitude;
    float longitude;
    String geohash;
    int occurences;

    /**
     * Constructeur de la classe Coordonnees.
     * @param latitude
     * @param longitude
     * @param geohash
     * @param occurences
     */
    public Coordonnees(float latitude, float longitude, String geohash, int occurences){
        this.latitude=latitude;
        this.longitude=longitude;
        this.geohash=geohash;
        this.occurences=occurences;
    }



}