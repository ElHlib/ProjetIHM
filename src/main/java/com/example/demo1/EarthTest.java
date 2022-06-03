package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;

public class EarthTest extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {

        //Create a Pane et graph scene root for the 3D content

        Parent content = FXMLLoader.load(getClass().getResource("earthGUI.fxml"));
        primaryStage.setTitle("Earth Test");
        primaryStage.setScene(new Scene(content));
        primaryStage.show();
        // Load geometry

    }


}
