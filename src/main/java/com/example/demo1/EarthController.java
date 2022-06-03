package com.example.demo1;
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
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.net.http.*;
import java.util.concurrent.TimeUnit;

public class EarthController implements Initializable {
    @FXML
    private Pane pane3D;

    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    private static final float TEXTURE_OFFSET = 1.01f;
    @FXML
    public void initialize(URL location, ResourceBundle resources){
        Group root3D = new Group();
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
            URL modelUrl = this.getClass().getResource("earth.obj");
            objImporter.read(modelUrl);
        } catch (ImportException e) {
            // handle exception
            System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);

        // Draw a line

        // Draw an helix

        // Draw city on the earth
        displayTown(earth, "Brest", (float) 48.447911, (float) -4.418539);
        displayTown(earth,"Marseille" , (float) 43.435555, (float) 5.213611);
        displayTown(earth, "New York" , (float) 40.639751, (float) -73.778925);
        displayTown(earth, "Cape Town", (float) -33.964806, (float) 18.601667);
        displayTown(earth,  "Istanbul", (float)40.976922 , (float)28.814606);
        displayTown(earth,  "Reykjavik" , (float)64.13 , (float)-21.940556);
        displayTown(earth, "Singapore" , (float)1.350189 , (float)103.994433);
        displayTown(earth,"Seoul" , (float)37.469075 , (float)126.450517);
        PhongMaterial yellow = new PhongMaterial();
        yellow.setDiffuseColor(Color.YELLOW);
        PhongMaterial green = new PhongMaterial();
        green.setDiffuseColor(Color.GREEN);
        /*
        boolean change = true;
        for(int lat = -90; lat<=90;lat+=10){
            for(int lon=-180;lon<=180;lon+=10){
                if (change) {
                    AddQuadrilateral(earth, geoCoordTo3dCoord(lat + 10, lon), geoCoordTo3dCoord(lat + 10, lon + 10),
                            geoCoordTo3dCoord(lat, lon + 10), geoCoordTo3dCoord(lat, lon), yellow);
                    change=false;
                }else{
                    AddQuadrilateral(earth, geoCoordTo3dCoord(lat + 10, lon), geoCoordTo3dCoord(lat + 10, lon + 10),
                            geoCoordTo3dCoord(lat, lon + 10), geoCoordTo3dCoord(lat, lon), green);
                    change=true;
                }
            }
        }
        */


        root3D.getChildren().add(earth);
        // Add a camera group


        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);
        // Create scene
        PerspectiveCamera camera = new PerspectiveCamera(true);
        SubScene subscene= new SubScene(root3D,600,600,true,SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subscene);
        try(Reader reader = new FileReader("data.json")){
            BufferedReader rd = new BufferedReader(reader);
            String jsonText =readAll(rd);
            JSONObject jsonRoot = new JSONObject(jsonText);
            JSONArray resultatRecherche = jsonRoot.getJSONObject("query").getJSONArray("search");
            JSONObject article = resultatRecherche.getJSONObject(0);
            System.out.println(article.getString("title"));
            System.out.println(article.getString("snippet"));
            JSONObject article2 = resultatRecherche.getJSONObject(1);
            System.out.println(article2.getString("title"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject whales=readJsonFromUrl("http://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=Whale&format=json");
        JSONArray resultatRecherche = whales.getJSONObject("query").getJSONArray("search");
        JSONObject article = resultatRecherche.getJSONObject(0);
        System.out.println(article.getString("title"));
        System.out.println(article.getString("snippet"));
        new CameraManager(camera, subscene.getRoot(), root3D);
        subscene.addEventHandler(MouseEvent.ANY, event->{
            if(event.getEventType()== MouseEvent.MOUSE_PRESSED && event.isShiftDown()){
                PickResult pickResult = event.getPickResult();
                Point3D spaceCoord = pickResult.getIntersectedPoint();
                Sphere sphere = new Sphere(0.01);
                Group g = new Group();
                PhongMaterial red = new PhongMaterial();
                red.setDiffuseColor(Color.RED);
                sphere.setMaterial(red);
                sphere.setTranslateX(spaceCoord.getX());
                sphere.setTranslateY(spaceCoord.getY());
                sphere.setTranslateZ(spaceCoord.getZ());
                g.getChildren().add(sphere);
                earth.getChildren().add(g);
                Point2D p2D = SpaceCoordToGeoCoord(spaceCoord);
                Location loc = new Location("selectedGeoHash", p2D.getX(),p2D.getY());
                System.out.println(GeoHashHelper.getGeohash(loc));
            }
        });


    }


    public static void displayTown(Group parent, String name,float latitude, float longitude){
        Point3D newcity= geoCoordTo3dCoord(latitude,longitude);
        Sphere sphere = new Sphere(0.01);
        Group g = new Group();
        PhongMaterial yellow = new PhongMaterial();
        yellow.setDiffuseColor(Color.YELLOW);
        sphere.setMaterial(yellow);
        sphere.setTranslateX(newcity.getX());
        sphere.setTranslateY(newcity.getY());
        sphere.setTranslateZ(newcity.getZ());
        g.setId(name);
        g.getChildren().add(sphere);
        parent.getChildren().add(g);

    }

    public static JSONObject readJsonFromUrl(String url){
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
    // From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }
    private static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp ;
        while((cp=rd.read())!= -1){
            sb.append((char)cp);
        }
        return sb.toString();
    }

    public static Point2D SpaceCoordToGeoCoord(Point3D p) {

        float lat = (float) (Math.asin(-p.getY() / TEXTURE_OFFSET)
                * (180 / Math.PI) - TEXTURE_LAT_OFFSET);
        float lon;

        if (p.getZ() < 0) {
            lon = 180 - (float) (Math.asin(-p.getX() / (TEXTURE_OFFSET
                    * Math.cos((Math.PI / 180)
                    * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI + TEXTURE_LON_OFFSET);
        } else {
            lon = (float) (Math.asin(-p.getX() / (TEXTURE_OFFSET * Math.cos((Math.PI / 180)
                    * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI - TEXTURE_LON_OFFSET);
        }

        return new Point2D(lat, lon);
    }
    public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        if( -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor+5))<=0) {
            return new Point3D(-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)), -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor + 2)), java.lang.Math.cos(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
        }else{
            return new Point3D(-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)), -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor -2)), java.lang.Math.cos(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
        }

    }

    private void AddQuadrilateral(Group parent, Point3D topRight, Point3D bottomRight,
                                  Point3D bottomLeft, Point3D topLeft, PhongMaterial material){
        final TriangleMesh triangleMesh = new TriangleMesh();
        final float[] points = {
                (float)topRight.getX(),(float)topRight.getY(),(float)topRight.getZ(),
                (float)topLeft.getX(),(float)topLeft.getY(),(float)topLeft.getZ(),
                (float)bottomLeft.getX(),(float)bottomLeft.getY(),(float)bottomLeft.getZ(),
                (float)bottomRight.getX(),(float)bottomRight.getY(),(float)bottomRight.getZ(),
        };
        final float[] texCoords = {
                1,1,
                1,0,
                0,1,
                0,0
        };
        final int[] faces = {
                0,1,1,0,2,2,
                0,1,2,2,3,3
        };
        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);

        final MeshView meshView = new MeshView(triangleMesh);
        meshView.setMaterial(material);
        parent.getChildren().addAll(meshView);

    }
}