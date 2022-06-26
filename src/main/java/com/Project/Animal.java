package com.Project;

import java.util.ArrayList;


public class Animal {
    String scientificname;
    ArrayList<ArrayList<Coordonnees>> Coordinates;
    int max=0;

    /**
     * Constructeur de la classe Animal.
     * @param name un string.
     */
    public Animal(String name){
        this.scientificname=name;
        this.Coordinates = new ArrayList<ArrayList<Coordonnees>>();

    }

    /**
     * Methode qui set le maximum d'occurences d'une espece
     * @param max un entier.
     */
    public void setMax(int max){
        this.max=max;
    }

    /**
     * Methode qui permet l'ajout d'un tableau de string des coordonnes animales.
     * @param coord un tableau de Coordonnees.
     */
    void addCoord(ArrayList<Coordonnees> coord){
        this.Coordinates.add(coord);
    }

}