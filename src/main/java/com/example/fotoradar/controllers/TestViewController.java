package com.example.fotoradar.controllers;

import com.example.fotoradar.components.Collection;
import com.example.fotoradar.components.CollectionRow;

import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collectible;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestViewController {
    @FXML
    public CollectionRow collectionRow;
    @FXML
    public Collection collection;

    CollectionOperations collectionOperations = new CollectionOperations();
    CollectibleOperations collectibleOperations = new CollectibleOperations();

    public TestViewController() throws SQLException {
    }

    public void initialize() throws SQLException, IOException {
        int id = 8;
        com.example.fotoradar.models.Collection collectionObj = collectionOperations.getCollectionById(id);
        collection.setHeaderLabel(collectionObj.getTitle());
        collection.setCollectibles(getCollectibles(id));
    }

    private ArrayList<Collectible> getCollectibles(int collectionId) throws SQLException {
        return collectibleOperations.getAllCollectibles(collectionId);
    }
}
