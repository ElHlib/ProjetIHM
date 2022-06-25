package com.example.Project;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.transform.Affine;
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
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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
    private ListView<String> TextEspece;
    @FXML
    private ListView<String> TextSignalement1;

    @FXML
    private Button btnPlay;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnStop;
    @FXML
    private ComboBox<String> btnPrecisions;
    @FXML
    private Button resetbtn;
    DonnesAnimalesJson currentAnimal = new DonnesAnimalesJson("Delphinidae");
    boolean histogramme = false;
    boolean playing=false;
    boolean correctespece = true;
    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    private static final float TEXTURE_OFFSET = 1.01f;
    int geohash =3;
    boolean reset = false;
    Group earth;
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
      ArrayList<Coordonner> coordi;
      int max = currentAnimal.getAnimal().max;
        for(int i=0;i<currentAnimal.getAnimal().Coordinates.size();i++) {
            coordi= currentAnimal.getAnimal().Coordinates.get(i);

            PhongMaterial mat = new PhongMaterial();
            if(coordi.get(0).occurences<=legendes(max)[0]){mat.setDiffuseColor(Color.ROYALBLUE);}
            else if(coordi.get(0).occurences<=legendes(max)[1]){mat.setDiffuseColor(Color.AQUAMARINE);}
            else if(coordi.get(0).occurences<=legendes(max)[2]){mat.setDiffuseColor(Color.PALEGREEN);}
            else if(coordi.get(0).occurences<=legendes(max)[3]){mat.setDiffuseColor(Color.KHAKI);}
            else if(coordi.get(0).occurences<=legendes(max)[4]){mat.setDiffuseColor(Color.ORANGE);}
            else if(coordi.get(0).occurences<=legendes(max)[5]){mat.setDiffuseColor(Color.MAROON);}
            AddQuadrilateral(root,geoCoordToPoint3D(coordi.get(2)),geoCoordToPoint3D(coordi.get(1)),geoCoordToPoint3D(coordi.get(0)),geoCoordToPoint3D(coordi.get(3)),mat);

        }
        Labelcolor0.setText("<="+max/12);
        Labelcolor1.setText("<="+max*3/12);
        Labelcolor2.setText("<="+max*3/6);
        Labelcolor3.setText("<="+max*9/12);
        Labelcolor4.setText("<="+max*5/6);
        Labelcolor5.setText("<="+max);
    }
    public int[] legendes(int max){
        int[] leg  = {max / 12, max * 3 / 12, max * 3 / 6, max * 9 / 12, max * 5 / 6, max};
        return(leg);
    }

    static Point3D geoCoordToPoint3D(Coordonner coord){
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



    public static Affine direction(Point3D initial) {

        Point3D z = Point3D.ZERO.subtract(initial).normalize();
        Point3D x = new Point3D(0, 1, 0).normalize().crossProduct(z).normalize();
        Point3D y = z.crossProduct(x).normalize();

        return new Affine(x.getX(), y.getX(), z.getX(), initial.getX(),
                x.getY(), y.getY(), z.getY(), initial.getY(),
                x.getZ(), y.getZ(), z.getZ(), initial.getZ());
    }

    public void DisplayHistogramme(Group root){
        ArrayList<Coordonner> coordi;

        float translateZ =0;
        int max = currentAnimal.getAnimal().max;
        for(int i=0;i<currentAnimal.getAnimal().Coordinates.size();i++) {
            coordi= currentAnimal.getAnimal().Coordinates.get(i);

            PhongMaterial mat = new PhongMaterial();

            if(coordi.get(0).occurences<=legendes(max)[0]){mat.setDiffuseColor(Color.ROYALBLUE);}
            else if(coordi.get(0).occurences<=legendes(max)[1]){mat.setDiffuseColor(Color.AQUAMARINE);}
            else if(coordi.get(0).occurences<=legendes(max)[2]){mat.setDiffuseColor(Color.PALEGREEN);}
            else if(coordi.get(0).occurences<=legendes(max)[3]){mat.setDiffuseColor(Color.KHAKI);}
            else if(coordi.get(0).occurences<=legendes(max)[4]){mat.setDiffuseColor(Color.ORANGE);}
            else if(coordi.get(0).occurences<=legendes(max)[5]){mat.setDiffuseColor(Color.MAROON);}
            Box box=null;
            if(geohash==1 || geohash==2){
                box = new Box(0.75f,0.75f,0.01f);
            }else if(geohash==3){
                box = new Box(0.025f,0.025f,0.01f);
            }
            else if(geohash==4){
                box = new Box(0.01f,0.01f,0.01f);
            }
            else if(geohash==5){
                box = new Box(0.005f,0.005f,0.01f);
            }
            box.setMaterial(mat);
            box.setDepth(coordi.get(0).occurences*(0.00005f*10000/max));
            box.setTranslateZ((-box.getDepth())/2);
            Affine affine = new Affine();
            Point3D initial = geoCoordToPoint3D(coordi.get(2)).midpoint(geoCoordToPoint3D(coordi.get(2)));
            affine.append(direction(initial));
            Group histo = new Group();
            histo.getChildren().add(box);
            histo.getTransforms().setAll(affine);
            root.getChildren().addAll(histo);
        }
        Labelcolor0.setText("<="+max/12);
        Labelcolor1.setText("<="+max*3/12);
        Labelcolor2.setText("<="+max*3/6);
        Labelcolor3.setText("<="+max*9/12);
        Labelcolor4.setText("<="+max*5/6);
        Labelcolor5.setText("<="+max);
    }

    public ArrayList<String> getGeoCoordClicked(Group root,MouseEvent clicked){
        PickResult pickResult = clicked.getPickResult();
        Point3D spaceCoord = pickResult.getIntersectedPoint();
        ArrayList<String> listesanimaux = new ArrayList<String>();
        Point2D geoCoord = CoordToPoint2D(spaceCoord);
        String geohashT = GeoHashHelper.getGeohash(new Localisation("selectedGeoHash", geoCoord.getX(), geoCoord.getY()));
        geohashT = geohashT.substring(0, 3);
        JSONObject objet = DonnesAnimalesJson.readUrl("https://api.obis.org/v3/occurrence?geometry="+geohashT);
        JSONArray especes = objet.getJSONArray("results");
        for(int i =0; i<especes.length();i++){
            if(listesanimaux.contains(especes.getJSONObject(i).getString("scientificName"))==false) {
                listesanimaux.add(especes.getJSONObject(i).getString("scientificName"));
            }
        }
        return listesanimaux;

    }

    @FXML
    public void initialize(URL location, ResourceBundle resources){
        Group root3D = new Group();
        earth=AfficherGlobe(root3D);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        SubScene subscene= new SubScene(root3D,450,450,true,SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        currentAnimal.readJsonFromFile("Delphinidae.json");
        combobox.setValue("Delphinidae");
        displaySpecies(earth);
        Pane3D.getChildren().addAll(subscene);
        color0.setStyle("-fx-background-color: #4169E1");
        color1.setStyle("-fx-background-color: #7FFFD4");
        color2.setStyle("-fx-background-color: #98FB98");
        color3.setStyle("-fx-background-color: #F0E68C");
        color4.setStyle("-fx-background-color: #FFA500");
        color5.setStyle("-fx-background-color: #800000");
        combobox.setEditable(true) ;
        combobox.setOnAction(event->{
            if(!playing && !reset && !combobox.getValue().equals(null)) {
                String espece = combobox.getValue();
                String name = espece.replaceAll("\\s+", "%20");
                JSONObject object = DonnesAnimalesJson.readUrl("https://api.obis.org/v3/checklist?scientificname=" + name);
                ObservableList<String> items = FXCollections.observableArrayList(completerNoms(espece));
                combobox.setItems(items);
                //combobox.setValue(espece);
                int total = object.getInt("total");
                if (total == 0) {
                    combobox.setStyle("-fx-background-color:orangered");
                    correctespece = false;
                } else if (DateDebut.getValue() != null && DateFin.getValue() != null) {
                    correctespece = true;
                    LocalDate datedebut = DateDebut.getValue();
                    LocalDate datefin = DateDebut.getValue();
                    earth.getChildren().subList(1, earth.getChildren().size()).clear();
                    combobox.setStyle("-fx-background-color:white");
                    currentAnimal = new DonnesAnimalesJson(espece);
                    currentAnimal.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/"+geohash+"?scientificname=" + name + "&startdate=" + datedebut + "&enddate" + datefin);
                    displaySpecies(earth);
                } else {
                    correctespece = true;
                    earth.getChildren().subList(1, earth.getChildren().size()).clear();
                    combobox.setStyle("-fx-background-color:white");
                    currentAnimal = new DonnesAnimalesJson(espece);
                    currentAnimal.readJsonFromUrl(currentAnimal.nameToUrl(espece,geohash));
                    displaySpecies(earth);
                }
            }
        });

        histogrammebutton.setOnAction(event->{
            if(histogramme){
                histogramme=false;
                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                displaySpecies(earth);
            }else{
                histogramme = true;
                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                DisplayHistogramme(earth);
            }
        });

        TextEspece.setOnMouseClicked(event->{
            if(TextEspece.getSelectionModel().getSelectedItem()!=null){
                correctespece=true;
                currentAnimal =new DonnesAnimalesJson(TextEspece.getSelectionModel().getSelectedItem());
                String nameurl = currentAnimal.nameToUrl(TextEspece.getSelectionModel().getSelectedItem(),geohash);
                currentAnimal.readJsonFromUrl(nameurl);
                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                combobox.setStyle("-fx-background-color:white");
                combobox.setValue(TextEspece.getSelectionModel().getSelectedItem());
                displaySpecies(earth);
            }
        });
        /*
        btnPlay.setOnAction(event-> {
            if (DateDebut.getValue() != null && DateFin.getValue() != null && playing == false && correctespece == true) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        playing = true;
                        LocalDate datedebut = DateDebut.getValue();
                        LocalDate datefin = DateFin.getValue();
                        LocalDate datecurrent = DateDebut.getValue();
                        String espece = combobox.getValue();
                        String name = espece.replaceAll("\\s+", "%20");
                        System.out.println(datecurrent.isBefore(datefin));
                        while (datecurrent.isBefore(datefin) == true) {
                            LocalDate finalDatecurrent = datecurrent;
                            Platform.runLater(() -> {
                                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                                currentAnimal = new DonnesAnimalesJson(combobox.getValue());
                                currentAnimal.readJsonFromUrl("https://api.obis.org/v3/occurrence/grid/" + geohash + "?scientificname=" + name + "&startdate=" + datedebut.toString() + "&enddate" + finalDatecurrent.toString());
                                System.out.println("test1");
                                displaySpecies(earth);
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            datecurrent = datecurrent.plusYears(5);
                            if (datecurrent.isAfter(datefin)) {
                                System.out.println("test2");
                                datecurrent = datefin;
                            }

                        }
                    }

                });
                thread.start();
                playing = false;

            }
        });
        */
            new CameraManager(camera, subscene.getRoot(), root3D);
            subscene.addEventHandler(MouseEvent.ANY, event -> {
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isShiftDown()) {
                    ArrayList<String> listeanimaux = getGeoCoordClicked(earth, event);
                    ObservableList<String> items = FXCollections.observableArrayList(listeanimaux);
                    TextEspece.setItems(items);
                } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isControlDown()) {
                    TextSignalement1.getItems().clear();
                    PickResult pickResult = event.getPickResult();
                    Point3D spaceCoord = pickResult.getIntersectedPoint();
                    Point2D geoCoord = CoordToPoint2D(spaceCoord);
                    String geohash = GeoHashHelper.getGeohash(new Localisation("selectedGeoHash", geoCoord.getX(), geoCoord.getY()));
                    geohash = geohash.substring(0, 3);
                    String name = currentAnimal.currentAnimal.scientificname.replaceAll("\\s+", "%20");
                    JSONObject objet = DonnesAnimalesJson.readUrl("https://api.obis.org/v3/occurrence?scientificname=" + name + "&geometry=" + geohash);
                    if (objet.getInt("total") > 0) {
                        System.out.println("test");
                        JSONArray results = objet.getJSONArray("results");
                        ArrayList<String> listesignalements = new ArrayList<>();
                        listesignalements.add("Scientificname   order   superclass   species");
                        String scientificName;
                        String order;
                        String superclass;
                        String species;
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject liste = results.getJSONObject(i);
                            try {
                                scientificName = liste.getString("scientificName");
                            }catch(Exception e){
                                scientificName = "Not found";
                            }
                            try {
                                order =liste.getString("order");
                            }catch(Exception e){
                                order = "Not found";
                            }
                            try {
                                superclass =liste.getString("superclass");
                            }catch(Exception e){
                                superclass = "Not found";
                            }
                            try {
                                species =liste.getString("species");
                            }catch(Exception e){
                                species = "Not found";
                            }



                            listesignalements.add(scientificName + "   " + order + "   " + superclass + "   " + species);
                        }
                        ObservableList<String> items = FXCollections.observableArrayList(listesignalements);
                        TextSignalement1.setItems(items);
                    }
                }
            });
            ObservableList<String> options =
                    FXCollections.observableArrayList(
                            "1",
                            "2",
                            "3",
                            "4",
                            "5"
                    );
            btnPrecisions.setItems(options);
            btnPrecisions.setValue("3");
            btnPrecisions.setOnAction(event->{
                String item  =btnPrecisions.getSelectionModel().getSelectedItem();
                geohash = Integer.parseInt(item);
            });
        resetbtn.setOnAction(event -> {
            try {
                earth.getChildren().subList(1, earth.getChildren().size()).clear();
                correctespece = false;
                histogramme=false;
                reset = true;
                playing = false;
                combobox.setValue("");
                new CameraManager(camera, subscene.getRoot(), root3D);
                TextEspece.setItems(null);
                reset = true;
                TextSignalement1.setItems(null);
                currentAnimal = new DonnesAnimalesJson("");

                Labelcolor0.setText("");
                Labelcolor1.setText("");
                Labelcolor2.setText("");
                Labelcolor3.setText("");
                Labelcolor4.setText("");
                Labelcolor5.setText("");


                reset = false;


            } catch (Exception e) {


                new CameraManager(camera, subscene.getRoot(), root3D);
                TextEspece.setItems(null);
                TextSignalement1.setItems(null);
                Labelcolor0.setText("");
                Labelcolor1.setText("");
                Labelcolor2.setText("");
                Labelcolor3.setText("");
                Labelcolor4.setText("");
                Labelcolor5.setText("");


            }
        });
        }
}
