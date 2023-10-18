package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Getter @Setter
public class CollectionViewController {
    public Label titleLabel;
    public ListView<Collectible> collectiblesListView;
    public Button backToCollectionsButton;
    public Button addCollectibleButton;
    public Button editCollectionButton;

    private Collection collection;

    @FXML
    public void initialize() throws SQLException {
        titleLabel.setText("kolekcje/ "+collection.getTitle());
        fillListView();
    }

    private void fillListView() throws SQLException {
        ArrayList<Collectible> collectibles = new CollectibleOperations().getAllCollectibles(collection.getId());

        for (Collectible collectible : collectibles)
            collectiblesListView.getItems().add(collectible);
    }

    @FXML
    private void backToCollections (ActionEvent event) throws IOException {
        new SwitchScene(event, "collections-view");
    }

    @FXML
    private void addCollectible (ActionEvent event) {

    }

    public void editCollection(ActionEvent event) throws IOException, SQLException {
        // Tworzenie kontrolera dla widoku edycji kolekcji i przekazywanie parametrów kolekcji
        EditCollectionViewController controller = new EditCollectionViewController();
        controller.setCollection(getCollection());

        // Przenoszenie do widoku kolekcji z przekazaną kolekcją
        new SwitchScene(event, "edit_collection_view", controller);
    }
}
