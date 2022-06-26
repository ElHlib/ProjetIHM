package com.example.Project;

public final class Localisation {
    private final String name;
    private final double latitude;
    private final double longitude;

    /**
     * Constructeur de la classe Localisation
     * @param name
     * @param latitude
     * @param longitude
     */
    public Localisation(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String name() {
        return name;
    }

    public double lat() {
        return latitude;
    }

    public double lng() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Location [name=" + name + ", latitude=" + latitude + ", longitude=" + longitude +  "]";
    }
}