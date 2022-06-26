package com.Project;


import org.json.JSONObject;

public class InfoSignalement {
    String Order;
    String superclass;

    String species;

    /**
     * Constructeur de la classe InfoSignalement
     * @param resultatRecherche
     */
    public InfoSignalement(JSONObject resultatRecherche){
        try{
            this.Order=resultatRecherche.getString("order");
        }catch(Exception e){
            this.Order="";
        }

        try{
            this.superclass=resultatRecherche.getString("superclass");
        }catch(Exception e){
            this.superclass="";
        }
        try{
            this.species=resultatRecherche.getString("species");
        }catch(Exception e){
            this.species="";
        }



    }
}