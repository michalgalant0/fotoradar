package com.example.fotoradar.controllers;

import com.example.fotoradar.Main;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML
    private void addCollection(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add_collection_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}
