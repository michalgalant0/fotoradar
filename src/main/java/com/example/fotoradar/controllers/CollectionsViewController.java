package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionsViewController {

    @FXML
    private Button addCollectionButton, openCollectionButton;
    @FXML
    private ListView<Collection> collectionsListView;

    @FXML
    public void initialize() throws SQLException {
        fillListView();
    }

    private void fillListView() throws SQLException {
        ArrayList<Collection> collections = new CollectionOperations().getAllCollections();

        for (Collection collection : collections)
            collectionsListView.getItems().add(collection);
    }

    @FXML
    private void addCollection(ActionEvent event) throws IOException {
        new SwitchScene(event, "add_collection_view");
    }

    @FXML
    private void openCollection(ActionEvent event) throws IOException {
        // pobiera aktualnie wybraną kolekcje z ListView
        Collection selectedCollection = collectionsListView.getSelectionModel().getSelectedItem();

        if (selectedCollection != null) {
            // Tworzenie kontrolera dla widoku kolekcji i przekazywanie kolekcji
            CollectionViewController controller = new CollectionViewController();
            controller.setCollection(selectedCollection);

            // Przenoszenie do widoku kolekcji z przekazaną kolekcją
            new SwitchScene(event, "collection_view", controller);
        }
    }
}
