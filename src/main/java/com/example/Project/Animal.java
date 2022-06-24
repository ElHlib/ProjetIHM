package com.example.Project;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Animal {
    String scientificname;
    String Speciesname;
    ArrayList<ArrayList<Coordonner>> Coordinates;
    ArrayList<InfoSignalement> Signalements;
    String dateMin;
    String dateMax;
    int max=0;
    public Animal(String name){
        this.scientificname=name;
        this.Coordinates = new ArrayList<ArrayList<Coordonner>>();

    }
    public void setMax(int max){
        this.max=max;
    }


    void addCoord(ArrayList<Coordonner> coord){
        this.Coordinates.add(coord);
    }
    void addSignalement(InfoSignalement Sig){
        this.Signalements.add(Sig);
    }
}
