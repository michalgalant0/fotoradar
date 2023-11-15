package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.views.CollectionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
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
    private ArrayList<Collectible> collectibles;

    public CollectionComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectionComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void setHeaderLabel(String name) {
        headerLabel.setText(name);
    }

    public void setCollectibles(ArrayList<Collectible> collectibles) {
        this.collectibles = collectibles;
    }

    public void fillCollectionVBox() throws IOException, SQLException {
        for (Collectible collectible : collectibles) {
            CollectionRowComponent collectionRow = new CollectionRowComponent();
            collectionRow.setCollectible(collectible);
            collectionRow.setNameLabel(collectible.getTitle());
            collectionRow.setStatusLabel("DO POBRANIA");
            collectionRow.setThumbnailPath(collection.getTitle());

            collectiblesContainer.getChildren().add(collectionRow);
        }
    }

    @FXML
    public void goToCollection(ActionEvent event) throws IOException {
        System.out.println("przejscie do kolekcji");
        System.out.println("CollectionComponent.goToCollection: "+collection);
        // utworzenie kontrolera widoku docelowego
        CollectionView collectionView = new CollectionView();
        collectionView.setCollection(collection);
        // zaladowanie nowej sceny z przekazanym kontrolerem
        new SwitchScene().switchScene(event, "collectionView", collectionView);
    }
}
