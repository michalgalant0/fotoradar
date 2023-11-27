package com.example.fotoradar.components;

import com.example.fotoradar.models.*;
import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class TeamRowComponent extends Node {
    @FXML
    public Label nameLabel;
    @FXML
    public TextArea descriptionTextArea;

    @Setter
    private Team team;
    @Setter
    private Collectible collectible;
    @Setter
    private Collection collection;

    public TeamRowComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamRowComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
    }

    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }

    public void setDescriptionTextArea(String description) {
        descriptionTextArea.setText(description);
    }




}
