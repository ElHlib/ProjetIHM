package com.example.demo1;


import org.json.JSONArray;
import org.json.JSONObject;

public class InfoSignalement {
    String Order;
    String superclass;
    String RecordedBy;
    String species;
    public InfoSignalement(String scientificname){
        JSONObject objet = DonnesAnimales.readUrl("https://api.obis.org/v3/taxon/"+scientificname);
        JSONObject resultatRecherche = objet.getJSONArray("results").getJSONObject(1);
        this.Order=resultatRecherche.getString("order");
        this.superclass=resultatRecherche.getString("infraphylum");
        this.RecordedBy=resultatRecherche.getString("")
    }
}
