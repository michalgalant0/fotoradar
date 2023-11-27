package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TeamFormComponent extends AnchorPane {
    @FXML
    public Label nameLabel;
    @FXML
    public TextArea descriptionTextArea;
    public TeamFormComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

}
