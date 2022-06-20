package com.example.demo1;
import javafx.event.Event;
import org.json.*;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.net.http.*;
import java.util.concurrent.TimeUnit;

public class EarthController implements Initializable {
    @FXML
    private Pane pane3D;
    DonnesAnimales currentAnimal = new DonnesAnimales("Delphinidae");
    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    private static final float TEXTURE_OFFSET = 1.01f;

    public Group AfficherGlobe(Group root3D){
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            URL modelUrl = this.getClass().getResource("earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e) {
            System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);
        PhongMaterial yellow = new PhongMaterial();
        yellow.setDiffuseColor(Color.YELLOW);
        PhongMaterial green = new PhongMaterial();
        green.setDiffuseColor(Color.GREEN);
        root3D.getChildren().add(earth);
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);
        return earth;
    }
    public void displaySpecies(Group root){
        ArrayList<Coord> coordi= null;
        for(int i=0;i<currentAnimal.getAnimal().Coordinates.size();i++) {
            coordi= currentAnimal.getAnimal().Coordinates.get(i);
            Double[] points;
            coordi.toArray(points);
            Point3D point3D = geoCoordToPoint3D(coordi);
            Polygon polygon = new Polygon(points);
            Group g = new Group();
            PhongMaterial red = new PhongMaterial();
            red.setDiffuseColor(Color.RED);
            sphere.setMaterial(red);
            sphere.setTranslateX(point3D.getX());
            sphere.setTranslateY(point3D.getY());
            sphere.setTranslateZ(point3D.getZ());
            g.getChildren().add(sphere);
            root.getChildren().add(g);
        }
    }

    public ArrayList<Animal> getGeoCoordClicked(Event cliked){
        return null;
    }
    Point3D geoCoordToPoint3D(Coord coord){
        float lat_cor = coord.latitude + TEXTURE_LAT_OFFSET;
        float lon_cor = coord.longitude + TEXTURE_LON_OFFSET;
        return new Point3D(-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)) * 1.01, -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor ))* 1.01, java.lang.Math.cos(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))* 1.01);
    }
    public Point2D CoordToPoint2D(Point3D coord){
        float lat = (float) (Math.asin(-coord.getY() / TEXTURE_OFFSET)
                * (180 / Math.PI) - TEXTURE_LAT_OFFSET);
        float lon;
        if (coord.getZ() < 0) {
            lon = 180 - (float) (Math.asin(-coord.getX() / (TEXTURE_OFFSET
                    * Math.cos((Math.PI / 180)
                    * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI + TEXTURE_LON_OFFSET);
        } else {
            lon = (float) (Math.asin(-coord.getX() / (TEXTURE_OFFSET * Math.cos((Math.PI / 180)
                    * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI - TEXTURE_LON_OFFSET);
        }
        return new Point2D(lat, lon);
    }









    @FXML
    public void initialize(URL location, ResourceBundle resources){
        Group root3D = new Group();
        Group earth =AfficherGlobe(root3D);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        SubScene subscene= new SubScene(root3D,600,600,true,SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        currentAnimal.readJsonFromFile("Delphinidae.json");
        displaySpecies(earth);
        pane3D.getChildren().addAll(subscene);
        new CameraManager(camera, subscene.getRoot(), root3D);
        subscene.addEventHandler(MouseEvent.ANY, event->{
            if(event.getEventType()== MouseEvent.MOUSE_PRESSED && event.isShiftDown()){
              getGeoCoordClicked(event);
            }
        });


    }
}