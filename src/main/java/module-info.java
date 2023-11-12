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
    exports com.example.fotoradar.databaseOperations;
    opens com.example.fotoradar.databaseOperations to javafx.fxml;

    opens com.example.fotoradar.controllers.components to javafx.fxml;
    opens com.example.fotoradar.controllers.windows to javafx.fxml;
    opens com.example.fotoradar.controllers.views to javafx.fxml;

    exports com.example.fotoradar.segmenter;
    exports com.example.fotoradar.listeners;
    opens com.example.fotoradar.listeners to javafx.fxml;
    exports com.example.fotoradar.controllers.contexts;
    opens com.example.fotoradar.controllers.contexts to javafx.fxml;
}