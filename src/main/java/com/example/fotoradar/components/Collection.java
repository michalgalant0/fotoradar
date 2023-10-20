package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class Collection extends AnchorPane {

    @FXML
    private Label headerLabel;

    @FXML
    private VBox collectiblesContainer;

    @FXML
    private ScrollPane scrollPane;

    private ArrayList<Collectible> collectibles;

    public Collection() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/Collection.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            CollectionRow collectionRow = new CollectionRow();
            collectionRow.setNameLabel(collectible.getTitle());

            collectiblesContainer.getChildren().add(collectionRow);
        }
    }

    @FXML
    public void goToCollection () {
        // todo implementacja akcji na wczytanie widoku kolekcji z daną kolekcją
        System.out.println("przejscie do kolekcji");
    }
}
