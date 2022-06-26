module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jimObjModelImporterJFX;
    requires org.json;
    requires java.net.http;


    opens com.Project to javafx.fxml;
    exports com.Project;

}