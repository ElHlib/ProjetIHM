package com.example.Project;

public class Coordonner {
    float latitude;
    float longitude;
    String geohash;
    int occurences;

    /**
     * constructeur de la class Coordonner.
     * @param latitude
     * @param longitude
     * @param geohash
     * @param occurences
     */
    public Coordonner(float latitude, float longitude, String geohash, int occurences){
        this.latitude=latitude;
        this.longitude=longitude;
        this.geohash=geohash;
        this.occurences=occurences;
    }

    /**
     * methode qui permet de changer la longitude et la latitude.
     * @param latitude
     * @param longitude
     */
    void setLatLon(float latitude, float longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    /**
     * methode qui permet de changer le geohash et donc la precision des informations affichees.
     * @param geohash
     */
    void setGeohash(String geohash){
        this.geohash=geohash;
    }
}
