package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import com.example.fotoradar.components.TeamRowComponent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Setter;

public class TeamComponent extends VBox {
    @FXML
    private Label nameLabel;
    @FXML
    private TextArea descriptionTextArea;

    @Setter
    private Team team;

    public TeamComponent() {
        // FXMLLoader nie jest potrzebny, ponieważ używamy VBox jako korzenia
    }

    public void initialize() {
        if (team != null) {
            nameLabel.setText(team.getName());
            descriptionTextArea.setText(team.getDescription());
        }
    }
}