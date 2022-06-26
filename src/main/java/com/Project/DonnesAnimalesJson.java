package com.Project;

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

    /**
     * Contructeur de la classe DonnesAnimalesJson.
     * @param name
     */
    public DonnesAnimalesJson(String name){
        currentAnimal= new Animal(name);
    }



    /**
     * Methode qui permet de get un animal.
     * @return current Animal recherchee.
     */
    Animal getAnimal(){
        return this.currentAnimal;
    }

    /**
     * Methode qui prend en valeur le nom d'animal ainsi que la precision d'affichage pour convertir ceux-ci en lien pour l'Api
     * @param name
     * @param geohash
     * @return
     */
    String nameToUrl(String name, int geohash){
        String res = "https://api.obis.org/v3/occurrence/grid/"+geohash+"?scientificname=";
        name = name.replaceAll("\\s+", "%20");
        res+=name;
        return res;
    }

    /**
     * Methode qui permet de recuperer le Json file a partir d'un lien Api
     * @param url
     * @return JSONObject
     */
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

    /**
     * Methode qui permet le traitement du Json issu d'un lien Api pour trouver les informations concernant les localisations de l'animal
     * @param url
     */
    void readJsonFromUrl(String url){
        JSONObject jsonweb=readUrl(url);
        JSONArray resultatRecherche = jsonweb.getJSONArray("features");
        for(int i =0;i<resultatRecherche.length();i++) {
            JSONObject article = resultatRecherche.getJSONObject(i);
            JSONArray coord = article.getJSONObject("geometry").getJSONArray("coordinates");
            int signalements = article.getJSONObject("properties").getInt("n");
            if(signalements>this.currentAnimal.max){this.currentAnimal.setMax(signalements);}
            for(int k=0;k<coord.length();k++){
                ArrayList<Coordonnees> coords = new ArrayList<Coordonnees>();
                for(int j=0;j<coord.getJSONArray(k).length();j++) {
                    float lon = Float.parseFloat(String.valueOf(coord.getJSONArray(k).getJSONArray(j).get(0)));
                    float lat = Float.parseFloat(String.valueOf(coord.getJSONArray(k).getJSONArray(j).get(1)));
                    Localisation loc = new Localisation("selectedGeoHash", lat, lon);
                    coords.add(new Coordonnees(lat, lon, GeoHashHelper.getGeohash(loc),signalements));
                }
                this.currentAnimal.addCoord(coords);
            }
        }
    }

    /**
     * Methode permettant de lire un BufferedReader issu d'un fichier JSON
     * @param rd
     * @return
     * @throws IOException
     */
    private static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp ;
        while((cp=rd.read())!= -1){
            sb.append((char)cp);
        }
        return sb.toString();
    }

    /**
     * Methode qui permet le traitement du Json issu d'un fichier pour trouver les informations concernant les localisations de l'animal
     * @param filename
     */
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
                    ArrayList<Coordonnees> coords = new ArrayList<Coordonnees>();
                    for(int j=0;j<coord.getJSONArray(k).length();j++) {
                        float lon = Float.parseFloat(String.valueOf(coord.getJSONArray(k).getJSONArray(j).get(0)));
                        float lat = Float.parseFloat(String.valueOf(coord.getJSONArray(k).getJSONArray(j).get(1)));
                        Localisation loc = new Localisation("selectedGeoHash", lat, lon);
                        coords.add(new Coordonnees(lat, lon, GeoHashHelper.getGeohash(loc),signalements));
                    }
                    this.currentAnimal.addCoord(coords);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Methode qui permet l'autocompletion des noms des differents animaux dans la barre de recherche
     * @param debut
     * @return une liste de String avec tous les animaux possible
     */
    public static ArrayList<String> completerNoms(String debut) {

        ArrayList<String> premiersNoms = new ArrayList<String>();
        String name = debut.replaceAll("\\s+", "%20");

        JSONArray json=readJsonArrayFromUrl("https://api.obis.org/v3/taxon/complete/verbose/" + name);

        for(int i =0 ; i<json.length(); i++) {

            premiersNoms.add(json.getJSONObject(i).get("scientificName").toString());

        }
        return premiersNoms;
    }

    /**
     * Methode permettant de recuperer un fichier JSON a partir d'une URL
     * @param url
     * @return
     */

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