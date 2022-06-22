package com.example.demo1;
import javafx.event.Event;
import javafx.scene.control.TextField;
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
import java.util.Arrays;
import java.util.ResourceBundle;
import java.net.http.*;
import java.util.concurrent.TimeUnit;

public class EarthController implements Initializable {
    @FXML
    private Pane Pane3D;
    @FXML
    private TextField TextEspece;
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
      ArrayList<Coord> coordi;
        for(int i=0;i<currentAnimal.getAnimal().Coordinates.size();i++) {
            coordi= currentAnimal.getAnimal().Coordinates.get(i);
            int max = currentAnimal.getAnimal().max;
            PhongMaterial mat = new PhongMaterial();
            if(coordi.get(0).occurences<=legendes(max)[0]){mat.setDiffuseColor(Color.ROYALBLUE);}
            else if(coordi.get(0).occurences<=legendes(max)[1]){mat.setDiffuseColor(Color.AQUAMARINE);System.out.println("1");}
            else if(coordi.get(0).occurences<=legendes(max)[2]){mat.setDiffuseColor(Color.PALEGREEN);System.out.println("2");}
            else if(coordi.get(0).occurences<=legendes(max)[3]){mat.setDiffuseColor(Color.KHAKI);System.out.println("3");}
            else if(coordi.get(0).occurences<=legendes(max)[4]){mat.setDiffuseColor(Color.ORANGE);System.out.println("4");}
            else if(coordi.get(0).occurences<=legendes(max)[5]){mat.setDiffuseColor(Color.MAROON);System.out.println("5");}
            AddQuadrilateral(root,geoCoordToPoint3D(coordi.get(2)),geoCoordToPoint3D(coordi.get(1)),geoCoordToPoint3D(coordi.get(0)),geoCoordToPoint3D(coordi.get(3)),mat);
        }
    }
    public int[] legendes(int max){
        int[] leg  = {max / 6, max * 3 / 12, max * 3 / 6, max * 9 / 12, max * 5 / 6, max};
        return(leg);
    }
    public ArrayList<Animal> getGeoCoordClicked(Event cliked){
        return null;
    }
    static Point3D geoCoordToPoint3D(Coord coord){
        float lat_cor = coord.latitude + TEXTURE_LAT_OFFSET;
        float lon_cor = coord.longitude + TEXTURE_LON_OFFSET;
        return new Point3D(-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)) * TEXTURE_OFFSET, -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor ))* TEXTURE_OFFSET, java.lang.Math.cos(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))* TEXTURE_OFFSET);
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





    private void AddQuadrilateral(Group parent, Point3D topRight, Point3D bottomRight, Point3D bottomLeft,
                                  Point3D topLeft, PhongMaterial material) {

        final TriangleMesh triangleMesh = new TriangleMesh();

        final float[] points = {
                (float) topRight.getX(), (float) topRight.getY(), (float) topRight.getZ(),
                (float) topLeft.getX(), (float) topLeft.getY(), (float) topLeft.getZ(),
                (float) bottomLeft.getX(), (float) bottomLeft.getY(), (float) bottomLeft.getZ(),
                (float) bottomRight.getX(), (float) bottomRight.getY(), (float) bottomRight.getZ()
        };

        final float[] texCoords = {
                1, 1,
                1, 0,
                0, 1,
                0, 0
        };

        final int[] faces = {
                0, 1, 1, 0, 2, 2,
                0, 1, 2, 2, 3, 3
        };

        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);

        final MeshView meshView = new MeshView(triangleMesh);
        meshView.setMaterial(material);
        parent.getChildren().add(meshView);

    }



    @FXML
    public void initialize(URL location, ResourceBundle resources){
        Group root3D = new Group();
        Group earth =AfficherGlobe(root3D);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        SubScene subscene= new SubScene(root3D,450,450,true,SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        currentAnimal.readJsonFromFile("Delphinidae.json");
        displaySpecies(earth);
        Pane3D.getChildren().addAll(subscene);
        TextEspece.set(event->{
            String espece = TextEspece.getText();

        });
        new CameraManager(camera, subscene.getRoot(), root3D);
        subscene.addEventHandler(MouseEvent.ANY, event->{
            if(event.getEventType()== MouseEvent.MOUSE_PRESSED && event.isShiftDown()){
              getGeoCoordClicked(event);
            }
        });


    }
}