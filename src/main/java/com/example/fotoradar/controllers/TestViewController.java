package com.example.fotoradar.controllers;

import com.example.fotoradar.components.Collectible;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import javafx.fxml.FXML;

import java.sql.SQLException;

public class TestViewController {
    @FXML
    public Collectible collectible;

    CollectibleOperations collectibleOperations = new CollectibleOperations();

    public TestViewController() throws SQLException {
    }

    public void initialize() throws SQLException {
        com.example.fotoradar.models.Collectible object = collectibleOperations.getCollectibleById(3);
        collectible.setCollectible(object);
    }

}
