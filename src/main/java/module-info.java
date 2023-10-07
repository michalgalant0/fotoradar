module com.example.fotoradar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires java.sql;

    opens com.example.fotoradar to javafx.fxml;
    exports com.example.fotoradar;
    exports com.example.fotoradar.controllers;
    opens com.example.fotoradar.controllers to javafx.fxml;
}