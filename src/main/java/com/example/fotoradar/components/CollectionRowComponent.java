package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

public class CollectionRowComponent extends AnchorPane {
    @FXML
    public ImageView objectThumbnailImageView;
    @FXML
    public Label nameLabel;
    @FXML
    public Label statusLabel;

    @Setter
    private Collectible collectible;

    private String thumbnailPath;

    public CollectionRowComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectionRowComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
    }

    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }

    public void setStatusLabel(String status) {
        statusLabel.setText(status);
    }

    public void setThumbnailPath(String collectionName) throws SQLException {
        thumbnailPath = String.format("%s/KOLEKCJE/%s/OBIEKTY/%s/MINIATURY/%s",
                System.getProperty("user.dir"), collectionName, collectible.getTitle(),
                new ThumbnailOperations().getAllThumbnails(collectible.getId()).get(0).getFileName());
    }

    public void setObjectThumbnailImageView() {
        Image image = new Image("file://"+thumbnailPath);
        objectThumbnailImageView.setImage(image);
    }
}
