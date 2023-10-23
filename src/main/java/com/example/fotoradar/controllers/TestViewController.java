package com.example.fotoradar.controllers;

import com.example.fotoradar.components.Collectibles;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import javafx.fxml.FXML;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestViewController {
    @FXML
    public Collectibles collectibles;

    CollectibleOperations collectibleOperations = new CollectibleOperations();

    public TestViewController() throws SQLException {
    }

    public void initialize() throws SQLException {
        ArrayList<com.example.fotoradar.models.Collectible> collectiblesList = collectibleOperations.getAllCollectibles(8);
        collectibles.setCollectibles(collectiblesList);
        collectibles.fillCollectiblesHBox();
    }

}
