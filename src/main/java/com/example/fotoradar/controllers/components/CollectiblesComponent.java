package com.example.fotoradar.controllers.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Thumbnail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

public class CollectiblesComponent extends AnchorPane {

    @FXML
    private HBox collectiblesContainer;

    @Setter
    private ArrayList<Collectible> collectibles;
    @Setter
    private ArrayList<Thumbnail> thumbnails;

    public CollectiblesComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectiblesComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillCollectiblesHBox() throws IOException {
        for (Collectible collectible : collectibles) {
            System.out.println("CollectiblesComponent.fillCollectiblesHBox: "+collectible);
            // utworzenie komponentu obiektu
            CollectibleComponent collectibleComponent = new CollectibleComponent();
            collectibleComponent.setCollectible(collectible);

            collectibleComponent.setMainThumbnail(
                    thumbnails.stream()
                            .filter(thumbnail -> thumbnail.getParentId() == collectible.getId())
                            .toList().get(0));
            collectibleComponent.fillComponent();

            collectiblesContainer.getChildren().add(collectibleComponent);
        }
    }
}
