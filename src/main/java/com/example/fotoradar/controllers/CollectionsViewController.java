package com.example.fotoradar.controllers;

import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionsViewController {

    @FXML
    private Button addCollectionButton;
    @FXML
    private ListView collectionsListView;

    @FXML
    public void initialize() throws SQLException {
        fillListView();
    }

    private void fillListView() throws SQLException {
        ArrayList<Collection> collections = new CollectionOperations().getAllCollections();

        for (Collection collection : collections)
            collectionsListView.getItems().add(
                    collection.getTitle() + " start: " + collection.getStartDate() + " koniec: " + collection.getFinishDate()
            );
    }

    // todo przenoszenie na drugi widok po wciśnięciu przycisku
    @FXML
    private void addCollection(ActionEvent event) {
    }
}
