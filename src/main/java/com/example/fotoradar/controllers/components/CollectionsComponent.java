package com.example.fotoradar.controllers.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Thumbnail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CollectionsComponent
        extends AnchorPane {
    @FXML
    private HBox collectionsContainer;

    @Setter
    private ArrayList<Collection> collections;
    @Setter
    private ArrayList<Collectible> collectibles;
    @Setter
    private ArrayList<Thumbnail> thumbnails;

    public CollectionsComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectionsComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillCollectionsHBox() throws IOException {
        System.err.println("CollectionsComponent.fill");
        for (Collection collection : collections) {
            // utworzenie komponentu kolekcja
            CollectionComponent collectionComponent = new CollectionComponent();
            collectionComponent.setCollection(collection);

            ArrayList<Collectible> collectibles = this.collectibles.stream()
                    .filter(
                            collectible -> collectible.getParentCollectionId() == collection.getId()
                    ).collect(Collectors.toCollection(ArrayList::new));
            collectionComponent.setCollectibles(collectibles);

            collectionComponent.setThumbnails(thumbnails);

            collectionComponent.setHeaderLabel(collection.getTitle());
            collectionComponent.fillCollectionVBox();

            collectionsContainer.getChildren().add(collectionComponent);
        }
    }
}
