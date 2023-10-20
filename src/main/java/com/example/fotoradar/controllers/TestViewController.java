package com.example.fotoradar.controllers;

import com.example.fotoradar.components.CollectionRow;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;

public class TestViewController {
    @FXML
    public CollectionRow collectionRow = null;

    CollectibleOperations cop = new CollectibleOperations();

    public TestViewController() throws SQLException {
    }

    public void initialize() throws SQLException, IOException {
        collectionRow.nameLabel.setText(getCollectibleName());
    }

    private String getCollectibleName() throws SQLException {
        Collectible object = cop.getCollectibleById(14);
        System.out.println(object);
        return object.getTitle();
    }
}
