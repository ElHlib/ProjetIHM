package com.example.Project;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.transform.Affine;
import javafx.util.Pair;
import org.json.*;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.Project.DonnesAnimalesJson.completerNoms;

public class EarthController implements Initializable {
    @FXML
    private Pane Pane3D;
    @FXML
    private DatePicker DateDebut;
    @FXML
    private DatePicker DateFin;
    @FXML
    private Pane color0;
    @FXML
    private Pane color1;
    @FXML
    private Pane color2;
    @FXML
    private Pane color3;
    @FXML
    private Pane color4;
    @FXML
    private Pane color5;
    @FXML
    private Label Labelcolor0;
    @FXML
    private Label Labelcolor1;
    @FXML
    private Label Labelcolor2;
    @FXML
    private Label Labelcolor3;
    @FXML
    private Label Labelcolor4;
    @FXML
    private Label Labelcolor5;
    @FXML
    private ComboBox<String> combobox;
    @FXML
    private Button histogrammebutton;
    @FXML
    private ListView<String> TextSignalement;

    @FXML
    private Button btnPlay;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnStop;
    @FXML
    private ComboBox<String> btnPrecisions;


    DonnesAnimalesJson currentAnimal = new DonnesAnimalesJson("Delphinidae");
    boolean histogramme = false;
    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    private static final float TEXTURE_OFFSET = 1.01f;

    public Group AfficherGlobe(Group root3D) {
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

    public void displaySpecies(Group root) {
        ArrayList<Coordonner> coordi;
        int max = currentAnimal.getAnimal().max;
        for (int i = 0; i < currentAnimal.getAnimal().Coordinates.size(); i++) {
            coordi = currentAnimal.getAnimal().Coordinates.get(i);

            PhongMaterial mat = new PhongMaterial();
            if (coordi.get(0).occurences <= legendes(max)[0]) {
                mat.setDiffuseColor(Color.ROYALBLUE);
            } else if (coordi.get(0).occurences <= legendes(max)[1]) {
                mat.setDiffuseColor(Color.AQUAMARINE);
            } else if (coordi.get(0).occurences <= legendes(max)[2]) {
                mat.setDiffuseColor(Color.PALEGREEN);
            } else if (coordi.get(0).occurences <= legendes(max)[3]) {
                mat.setDiffuseColor(Color.KHAKI);
            } else if (coordi.get(0).occurences <= legendes(max)[4]) {
                mat.setDiffuseColor(Color.ORANGE);
            } else if (coordi.get(0).occurences <= legendes(max)[5]) {
                mat.setDiffuseColor(Color.MAROON);
            }
            AddQuadrilateral(root, geoCoordToPoint3D(coordi.get(2)), geoCoordToPoint3D(coordi.get(1)), geoCoordToPoint3D(coordi.get(0)), geoCoordToPoint3D(coordi.get(3)), mat);
        }
        Labelcolor0.setText("<=" + max / 12);
        Labelcolor1.setText("<=" + max * 3 / 12);
        Labelcolor2.setText("<=" + max * 3 / 6);
        Labelcolor3.setText("<=" + max * 9 / 12);
        Labelcolor4.setText("<=" + max * 5 / 6);
        Labelcolor5.setText("<=" + max);
    }

    public int[] legendes(int max) {
        int[] leg = {max / 12, max * 3 / 12, max * 3 / 6, max * 9 / 12, max * 5 / 6, max};
        return (leg);
    }

    static Point3D geoCoordToPoint3D(Coordonner coord) {
        float lat_cor = coord.latitude + TEXTURE_LAT_OFFSET;
        float lon_cor = coord.longitude + TEXTURE_LON_OFFSET;
        return new Point3D(-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)) * TEXTURE_OFFSET, -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)) * TEXTURE_OFFSET, java.lang.Math.cos(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)) * TEXTURE_OFFSET);
    }

    public Point2D CoordToPoint2D(Point3D coord) {
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


    public static Affine lookAt(Point3D from, Point3D to, Point3D ydir) {

        Point3D zVec = to.subtract(from).normalize();
        Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
        Point3D yVec = zVec.crossProduct(xVec).normalize();

        return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
                xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
                xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
    }

    public Group displayPoint(Point3D coord, Group root) {

        Sphere sphere = new Sphere(0.005);

        sphere.setTranslateX(coord.getX());
        sphere.setTranslateY(coord.getY());
        sphere.setTranslateZ(coord.getZ());

        root.getChildren().add(sphere);

        return root;
    }

    public void DisplayHistogramme(Group root) {
        ArrayList<Coordonner> coordi;

        float translateZ = 0;
        int max = currentAnimal.getAnimal().max;
        for (int i = 0; i < currentAnimal.getAnimal().Coordinates.size(); i++) {
            coordi = currentAnimal.getAnimal().Coordinates.get(i);

            PhongMaterial mat = new PhongMaterial();

            if (coordi.get(0).occurences <= legendes(max)[0]) {
                mat.setDiffuseColor(Color.ROYALBLUE);
            } else if (coordi.get(0).occurences <= legendes(max)[1]) {
                mat.setDiffuseColor(Color.AQUAMARINE);
            } else if (coordi.get(0).occurences <= legendes(max)[2]) {
                mat.setDiffuseColor(Color.PALEGREEN);
            } else if (coordi.get(0).occurences <= legendes(max)[3]) {
                mat.setDiffuseColor(Color.KHAKI);
            } else if (coordi.get(0).occurences <= legendes(max)[4]) {
                mat.setDiffuseColor(Color.ORANGE);
            } else if (coordi.get(0).occurences <= legendes(max)[5]) {
                mat.setDiffuseColor(Color.MAROON);
            }
            Box box = new Box(0.025f, 0.025f, 0.01f);
            box.setMaterial(mat);
            box.setDepth(0.0001f * coordi.get(0).occurences * 8000 / max);
            box.setTranslateZ((-box.getDepth()) / 2);
            Affine affine = new Affine();
            Point3D from = geoCoordToPoint3D(coordi.get(2)).midpoint(geoCoordToPoint3D(coordi.get(2)));
            Point3D to = Point3D.ZERO;
            Point3D yDir = new Point3D(0, 1, 0);
            affine.append(lookAt(from, to, yDir));

            Group group = new Group();
            group.getChildren().add(box);

            group.getTransforms().setAll(affine);
            root.getChildren().addAll(group);


        }
        Labelcolor0.setText("<=" + max / 12);
        Labelcolor1.setText("<=" + max * 3 / 12);
        Labelcolor2.setText("<=" + max * 3 / 6);
        Labelcolor3.setText("<=" + max * 9 / 12);
        Labelcolor4.setText("<=" + max * 5 / 6);
        Labelcolor5.setText("<=" + max);
    }

    public ArrayList<String> getGeoCoordClicked(Group root, MouseEvent clicked) {
        PickResult pickResult = clicked.getPickResult();
        Point3D spaceCoord = pickResult.getIntersectedPoint();
        ArrayList<String> listesanimaux = new ArrayList<String>();
        displayPoint(spaceCoord, root);
        Point2D geoCoord = CoordToPoint2D(spaceCoord);
        String geohash = GeoHashHelper.getGeohash(new Localisation("selectedGeoHash", geoCoord.getX(), geoCoord.getY()));
        geohash = geohash.substring(0, 3);

        JSONObject objet = DonnesAnimalesJson.readUrl("https://api.obis.org/v3/occurrence?geometry=" + geohash);

        JSONArray especes = objet.getJSONArray("results");


        for (int i = 0; i < especes.length(); i++) {
            if (listesanimaux.contains(especes.getJSONObject(i).getString("scientificName")) == false) {
                listesanimaux.add(especes.getJSONObject(i).getString("scientificName"));
            }
        }
        return listesanimaux;

    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Group root3D = new Group();
        Group earth = AfficherGlobe(root3D);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        SubScene subscene = new SubScene(root3D, 500, 500, true, SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        currentAnimal.readJsonFromFile("Delphinidae.json");
        displaySpecies(earth);
        Pane3D.getChildren().addAll(subscene);
        color0.setStyle("-fx-background-color: #4169E1");
        color1.setStyle("-fx-background-color: #7FFFD4");
        color2.setStyle("-fx-background-color: #98FB98");
        color3.setStyle("-fx-background-color: #F0E68C");
        color4.setStyle("-fx-background-color: #FFA500");
        color5.setStyle("-fx-background-color: #800000");

        combobox.setEditable(true);

        combobox.setOnAction(event -> {
            String espece = combobox.getValue();
            String name = espece.replaceAll("\\s+", "%20");
            JSONObject object = DonnesAnimalesJson.readUrl("https://api.obis.org/v3/checklist?scientificname=" + name);
            ObservableList<String> items = FXCollections.observableArrayList(completerNoms(combobox.getValue()));
            combobox.setItems(items);
            //combobox.setValue(espece);


            int total = object.getInt("total");

            if (total == 0) {
                combobox.setStyle("-fx-background-color:orangered");

            } else {

                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                combobox.setStyle("-fx-background-color:white");
                currentAnimal = new DonnesAnimalesJson(combobox.getValue());
                currentAnimal.readJsonFromUrl(currentAnimal.nameToUrl(combobox.getValue()));
                displaySpecies(earth);

            }


        });
        histogrammebutton.setOnAction(event -> {
            if (histogramme) {
                histogramme = false;
                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                displaySpecies(earth);
            } else {
                histogramme = true;
                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                DisplayHistogramme(earth);
            }
        });
        TextSignalement.setOnMouseClicked(event -> {
            if (TextSignalement.getSelectionModel().getSelectedItem() != null) {

                currentAnimal = new DonnesAnimalesJson(TextSignalement.getSelectionModel().getSelectedItem());
                String nameurl = currentAnimal.nameToUrl(TextSignalement.getSelectionModel().getSelectedItem());
                currentAnimal.readJsonFromUrl(nameurl);
                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                combobox.setStyle("-fx-background-color:white");
                combobox.setValue(TextSignalement.getSelectionModel().getSelectedItem());
                displaySpecies(earth);
            }
        });
        new CameraManager(camera, subscene.getRoot(), root3D);
        subscene.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isShiftDown()) {
                ArrayList<String> listeanimaux = getGeoCoordClicked(earth, event);
                ObservableList<String> items = FXCollections.observableArrayList(listeanimaux);
                TextSignalement.setItems(items);
            }
        });

        //set btnPrecisions to have a list from 1 to 8
        ObservableList<String> options =
                FXCollections.observableArrayList(

                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8"

                );

        btnPrecisions.setItems(options);




    }
}