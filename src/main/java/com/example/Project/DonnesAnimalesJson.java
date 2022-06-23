package com.example.Project;

import javafx.geometry.Point2D;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class DonnesAnimalesJson {
    Animal currentAnimal;
    public DonnesAnimalesJson(String name){
        currentAnimal= new Animal(name);
    }
    void SetAnimal(Animal animal){
        this.currentAnimal=animal;
    }
    Animal getAnimal(){
        return this.currentAnimal;
    }

    String nameToUrl(String name){
        String res = "https://api.obis.org/v3/occurrence/grid/3?scientificname=";
        name = name.replaceAll("\\s+", "%20");
        res+=name;
        return res;
    }
    public static JSONObject readUrl(String url){
        String json = "";
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type","application/json")
                .GET()
                .build();
        try{
            json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        }catch(Exception e ){
            e.printStackTrace();
        }
        return new JSONObject(json);
    }

    void readJsonFromUrl(String url){
        JSONObject jsonweb=readUrl(url);
        JSONArray resultatRecherche = jsonweb.getJSONArray("features");
        for(int i =0;i<resultatRecherche.length();i++) {
            JSONObject article = resultatRecherche.getJSONObject(i);
            JSONArray coord = article.getJSONObject("geometry").getJSONArray("coordinates");
            int signalements = article.getJSONObject("properties").getInt("n");
            if(signalements>this.currentAnimal.max){this.currentAnimal.setMax(signalements);}
            for(int k=0;k<coord.length();k++){
                ArrayList<Coordonner> coords = new ArrayList<Coordonner>();
                for(int j=0;j<coord.getJSONArray(k).length();j++) {
                    float lon = Float.parseFloat(String.valueOf(coord.getJSONArray(k).getJSONArray(j).get(0)));
                    float lat = Float.parseFloat(String.valueOf(coord.getJSONArray(k).getJSONArray(j).get(1)));
                    Localisation loc = new Localisation("selectedGeoHash", lat, lon);
                    coords.add(new Coordonner(lat, lon, GeoHashHelper.getGeohash(loc),signalements));
                }
                this.currentAnimal.addCoord(coords);
            }
        }
    }
    private static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp ;
        while((cp=rd.read())!= -1){
            sb.append((char)cp);
        }
        return sb.toString();
    }
    void readJsonFromFile(String filename){

        try(Reader reader = new FileReader(filename)){
            BufferedReader rd = new BufferedReader(reader);
            String jsonText =readAll(rd);
            JSONObject jsonRoot = new JSONObject(jsonText);
            JSONArray resultatRecherche = jsonRoot.getJSONArray("features");
            for(int i =0;i<resultatRecherche.length();i++) {
                JSONObject article = resultatRecherche.getJSONObject(i);
                JSONArray coord = article.getJSONObject("geometry").getJSONArray("coordinates");
                int signalements = article.getJSONObject("properties").getInt("n");
                if(signalements>this.currentAnimal.max){this.currentAnimal.setMax(signalements);}
                for(int k=0;k<coord.length();k++){
                    ArrayList<Coordonner> coords = new ArrayList<Coordonner>();
                    for(int j=0;j<coord.getJSONArray(k).length();j++) {
                        float lon = Float.parseFloat(String.valueOf(coord.getJSONArray(k).getJSONArray(j).get(0)));
                        float lat = Float.parseFloat(String.valueOf(coord.getJSONArray(k).getJSONArray(j).get(1)));
                        Localisation loc = new Localisation("selectedGeoHash", lat, lon);
                        coords.add(new Coordonner(lat, lon, GeoHashHelper.getGeohash(loc),signalements));
                    }
                    this.currentAnimal.addCoord(coords);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    Animal JSONObjectToAnimal(){
        return null;
    }
    String GeoGPStoGeohash(Point2D point){
        return null;
    }

    public static ArrayList<String> completerNoms(String debut) {

        ArrayList<String> premiersNoms = new ArrayList<String>();
        String name = debut.replaceAll("\\s+", "%20");

        JSONArray json=readJsonArrayFromUrl("https://api.obis.org/v3/taxon/complete/verbose/" + name);

        for(int i =0 ; i<json.length(); i++) {

            premiersNoms.add(json.getJSONObject(i).get("scientificName").toString());

        }
        return premiersNoms;
    }
    public static JSONArray readJsonArrayFromUrl(String url) {

        String json = "";

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            json=client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return new JSONArray(json);
    }

}
