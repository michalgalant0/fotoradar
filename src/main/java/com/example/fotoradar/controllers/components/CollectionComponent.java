package com.example.fotoradar.controllers.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.controllers.contexts.CollectionViewContext;
import com.example.fotoradar.controllers.contexts.MainContext;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Thumbnail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

public class CollectionComponent extends AnchorPane {
    @FXML
    private Label headerLabel;
    @FXML
    private VBox collectiblesContainer;
    @FXML
    private ScrollPane scrollPane;

    @Setter
    private Collection collection;
    @Setter
    private ArrayList<Collectible> collectibles;
    @Setter
    private ArrayList<Thumbnail> thumbnails;

    public CollectionComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectionComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void setHeaderLabel(String name) {
        headerLabel.setText(name);
    }

    public void fillCollectionVBox() throws IOException {
        for (Collectible collectible : collectibles) {
            CollectionRowComponent collectionRow = new CollectionRowComponent();
            collectionRow.setCollectible(collectible);
            collectionRow.setMainThumbnail(thumbnails.stream()
                    .filter(thumbnail -> thumbnail.getParentId() == collectible.getId())
                    .toList().get(0));
            collectionRow.setNameLabel(collectible.getTitle());
            collectionRow.setStatusLabel("DO POBRANIA");
            collectionRow.setObjectThumbnailImageView(collection.getTitle());

            collectiblesContainer.getChildren().add(collectionRow);
        }
    }

    // todo ustawić currentColletion na kontekscie przy przechodzeniu (bez przekazywania kontrolera, widok pobierze z kontekstu)
    @FXML
    public void goToCollection(ActionEvent event) throws IOException {
        System.out.println("przejscie do kolekcji");
        System.out.println("CollectionComponent.goToCollection: "+collection);
        CollectionViewContext.getInstance().setCurrentCollection(collection);
        new SwitchScene().switchScene(event, "collectionView");
    }
}
