package com.example.fotoradar.controllers;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionsViewController {

    @FXML
    private Button addCollectionButton, openCollectionButton;
    @FXML
    private ListView collectionsListView;

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
        Collection selectedCollection = (Collection) collectionsListView.getSelectionModel().getSelectedItem();
        System.out.println(selectedCollection); //debug

        // tmp
        new SwitchScene(event, "collection_view");
    }
}
