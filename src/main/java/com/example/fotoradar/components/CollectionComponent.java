package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class CollectionComponent extends AnchorPane {

    @FXML
    private Label headerLabel;

    @FXML
    private VBox collectiblesContainer;

    @FXML
    private ScrollPane scrollPane;

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

    public void setCollectibles(ArrayList<Collectible> collectibles) throws IOException {
        this.collectibles = collectibles;
        fillCollectionVBox();
    }

    private void fillCollectionVBox() throws IOException {
        for (Collectible collectible : collectibles) {
            CollectionRowComponent collectionRow = new CollectionRowComponent();
            collectionRow.setNameLabel(collectible.getTitle());

            collectiblesContainer.getChildren().add(collectionRow);
        }
    }

    @FXML
    public void goToCollection(ActionEvent event) throws IOException {
        System.out.println("przejscie do kolekcji");
        new SwitchScene().switchScene(event, "collectionView");
    }
}
