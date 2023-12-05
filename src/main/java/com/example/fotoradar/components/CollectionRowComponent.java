package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Thumbnail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionRowComponent extends AnchorPane {
    @FXML
    private StackPane imageContainer;
    @FXML
    public ImageView objectThumbnailImageView;
    @FXML
    public Label nameLabel;
    @FXML
    public Label statusLabel;

    @Setter
    private Collectible collectible;

    private String thumbnailPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/MINIATURY/%s";

    public CollectionRowComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectionRowComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
        // Set the alignment to center the image within the StackPane
        StackPane.setAlignment(objectThumbnailImageView, javafx.geometry.Pos.CENTER);

        // Set properties for adjusting the size to fit the ImageView
        objectThumbnailImageView.setPreserveRatio(true);
        objectThumbnailImageView.setFitWidth(imageContainer.getPrefWidth());  // Adjust the width as needed
        objectThumbnailImageView.setFitHeight(imageContainer.getPrefHeight()); // Adjust the height as needed
    }

    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }

    public void setStatusLabel(String status) {
        statusLabel.setText(status);
    }

    public void setThumbnailPath(String collectionName) throws SQLException {
        ArrayList<Thumbnail> allThumbnails = new ThumbnailOperations().getAllThumbnails(collectible.getId());
        if (!allThumbnails.isEmpty()) {
            thumbnailPath = String.format(thumbnailPath,
                    System.getProperty("user.dir"), collectionName, collectible.getTitle(),
                    allThumbnails.get(0).getFileName());
            setObjectThumbnailImageView();
        }
        else
            objectThumbnailImageView.setImage(null);
    }

    public void setObjectThumbnailImageView() {
        Image image = new Image("file://" + thumbnailPath);
        objectThumbnailImageView.setImage(image);
    }
}
