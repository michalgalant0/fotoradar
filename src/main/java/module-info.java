module com.example.fotoradar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.fotoradar to javafx.fxml;
    exports com.example.fotoradar;
    exports com.example.fotoradar.databaseOperations;
    opens com.example.fotoradar.databaseOperations to javafx.fxml;

    opens com.example.fotoradar.components to javafx.fxml;
    opens com.example.fotoradar.windows to javafx.fxml;
    opens com.example.fotoradar.views to javafx.fxml;

    exports com.example.fotoradar.segmenter;
}