package com.example.demo1;

import java.util.ArrayList;


public class Animal {
    String scientificname;
    String Speciesname;
    ArrayList<Coord> Coordinates;
    ArrayList<InfoSignalement> Signalements;
    String dateMin;
    String dateMax;
    public Animal(){
        this.Coordinates = new ArrayList<Coord>();
        this.Signalements= new ArrayList<InfoSignalement>();
    }
    void addCoord(Coord coord){
        this.Coordinates.add(coord);
    }
    void addSignalement(InfoSignalement Sig){
        this.Signalements.add(Sig);
    }
}
