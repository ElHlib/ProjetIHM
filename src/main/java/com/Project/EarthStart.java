package com.Project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;

public class EarthStart extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent content = FXMLLoader.load(getClass().getResource("earthGUI.fxml"));
        primaryStage.setTitle("Earth Test");
        primaryStage.setScene(new Scene(content));
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(850);
        primaryStage.show();

    }


}
