package com.example.Project;


import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.util.Pair;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class InfoSignalement {
    String Order;
    String superclass;
    String RecordedBy;
    String species;

    public InfoSignalement(JSONObject resultatRecherche) {
        try {
            this.Order = resultatRecherche.getString("order");
        } catch (Exception e) {
            this.Order = "";
        }

        try {
            this.superclass = resultatRecherche.getString("superclass");
        } catch (Exception e) {
            this.superclass = "";
        }
        try {
            this.RecordedBy = resultatRecherche.getString("recordedBy");
        } catch (Exception e) {
            this.RecordedBy = "";
        }
        try {
            this.species = resultatRecherche.getString("species");
        } catch (Exception e) {
            this.species = "";
        }

    }




}

