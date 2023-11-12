package com.example.fotoradar.controllers.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.controllers.views.View;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Thumbnail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

public class CollectionRowComponent extends AnchorPane implements View {
    @FXML
    public ImageView objectThumbnailImageView;
    @FXML
    public Label nameLabel;
    @FXML
    public Label statusLabel;

    @Setter
    private Collectible collectible;
    @Setter
    private Thumbnail mainThumbnail;

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

    public void setObjectThumbnailImageView(String collectionName) {
        if (mainThumbnail == null)
            return;
        String thumbnailPath = String.format(context.getThumbnailPath(),
                context.getRootDirectory(), collectionName, collectible.getTitle(),
                mainThumbnail.getFileName());
        Image image = new Image("file://"+thumbnailPath);
        objectThumbnailImageView.setImage(image);
    }
}
