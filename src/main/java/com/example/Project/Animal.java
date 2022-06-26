package com.example.Project;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Animal {
    String scientificname;
    ArrayList<ArrayList<Coordonner>> Coordinates;
    int max=0;

    /**
     * constructeur de la class Animal.
     * @param name un string.
     */
    public Animal(String name){
        this.scientificname=name;
        this.Coordinates = new ArrayList<ArrayList<Coordonner>>();

    }

    /**
     * methode qui set le max.
     * @param max un entier.
     */
    public void setMax(int max){
        this.max=max;
    }

    /**
     * methode qui permet l'ajout d'un tableau de string des coordonnes animales.
     * @param coord un tableau de Coordonner.
     */
    void addCoord(ArrayList<Coordonner> coord){
        this.Coordinates.add(coord);
    }

}
