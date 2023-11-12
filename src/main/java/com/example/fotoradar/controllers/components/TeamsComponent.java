package com.example.fotoradar.controllers.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TeamsComponent extends AnchorPane {
    public TeamsComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamsComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }
}
