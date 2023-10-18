package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

@Setter
public class AddCollectibleViewController {
    @FXML
    public Button addCollectibleButton;

    @FXML
    private TextField titleTextField;
    @FXML
    private TextField startDateTextField;
    @FXML
    private TextField finishDateTextField;

    @FXML
    private TextArea descriptionTextArea;
    private Collection collection;

    private final CollectibleOperations collectibleOperations = new CollectibleOperations();

    public AddCollectibleViewController() throws SQLException {
    }

    public void addCollectible(ActionEvent actionEvent) throws IOException {
        String title = titleTextField.getText();
        String startDate = startDateTextField.getText();
        String finishDate = finishDateTextField.getText();
        String description = descriptionTextArea.getText();
        int parentCollectionId = collection.getId();

        Collectible collectibleToAdd = new Collectible(title, startDate, finishDate, description, parentCollectionId);

        try {
            collectibleOperations.addCollectible(collectibleToAdd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Tworzenie kontrolera dla widoku kolekcji i przekazywanie kolekcji
        CollectionViewController controller = new CollectionViewController();
        controller.setCollection(collection);

        // Przenoszenie do widoku kolekcji z przekazaną kolekcją
        new SwitchScene(actionEvent, "collection_view", controller);
    }

    public void cancel(ActionEvent actionEvent) throws IOException {         // Tworzenie kontrolera dla widoku kolekcji i przekazywanie kolekcji
        CollectionViewController controller = new CollectionViewController();
        controller.setCollection(collection);

        // Przenoszenie do widoku kolekcji z przekazaną kolekcją
        new SwitchScene(actionEvent, "collection_view", controller);
    }
}
