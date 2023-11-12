package com.example.fotoradar;

import com.example.fotoradar.controllers.contexts.MainContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {;
        // main view
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/collectionsView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("fotoradar");
        stage.setScene(scene);
        stage.show();

        // ustawienie kontekstu aplikacji
        MainContext context = MainContext.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }
}