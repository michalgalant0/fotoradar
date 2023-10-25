package com.example.fotoradar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // using fxml loader for layout loading
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("windows/TEST-WINDOWS.fxml"));
        // main view
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/collectionsView.fxml"));
        // dla testow
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/collectionView.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("fotoradar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}