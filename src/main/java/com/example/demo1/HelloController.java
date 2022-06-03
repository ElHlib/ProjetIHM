package com.example.demo1;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Pane pane3D;

    @FXML
    public void initialize(URL location, ResourceBundle resources){
        Group root3D = new Group();
        double rotationSpeed=20;
        //Create cube shape
        Box cubebleu = new Box(1, 1, 1);
        Box cubevert = new Box(1, 1, 1);
        Box cubered = new Box(1, 1, 1);
        //Create Material
        final PhongMaterial blueMaterial = new PhongMaterial();
        final PhongMaterial greenMaterial = new PhongMaterial();
        final PhongMaterial redMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.RED);
        //Set it to the cube
        cubebleu.setMaterial(blueMaterial);
        cubevert.setMaterial(greenMaterial);
        cubered.setMaterial(redMaterial);
        cubevert.setTranslateX(0);
        cubevert.setTranslateY(-2);
        cubered.setTranslateX(2);
        cubered.setTranslateZ(1);
        //Add the cube to this node
        root3D.getChildren().add(cubebleu);
        root3D.getChildren().add(cubevert);
        root3D.getChildren().add(cubered);
        //Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);


        // Create scene
        SubScene subscene = new SubScene(root3D, 600 ,600,true, SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subscene);
        new CameraManager(camera,subscene.getRoot(),root3D);
        //Add the scene to the stage and show it
        final long startNanoTime = System.nanoTime();
        new AnimationTimer(){
            public void handle(long currentNanoTime){
                double t = (currentNanoTime-startNanoTime)/1000000000.0;
                cubevert.setRotationAxis(new Point3D(0,1,0));
                cubevert.setRotate(rotationSpeed*t);

            }
        }.start();

    }
}