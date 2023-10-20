package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CollectionRow extends AnchorPane {
    @FXML
    public ImageView objectThumbnailImageView;
    @FXML
    public Label nameLabel;
    @FXML
    public Label statusLabel;

    public CollectionRow() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectionRow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }
}
