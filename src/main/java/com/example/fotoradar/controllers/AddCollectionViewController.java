package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class AddCollectionViewController {

    @FXML
    public Button addCollectionButton;

    @FXML
    private TextField titleTextField;
    @FXML
    private TextField startDateTextField;
    @FXML
    private TextField finishDateTextField;

    @FXML
    private TextArea descriptionTextArea;

    private final CollectionOperations collectionOperations = new CollectionOperations();

    public AddCollectionViewController() throws SQLException {
    }

    public void addCollection(ActionEvent actionEvent) throws IOException {
        String title = titleTextField.getText();
        String startDate = startDateTextField.getText();
        String finishDate = finishDateTextField.getText();
        String description = descriptionTextArea.getText();

        Collection collectionToAdd = new Collection(title, startDate, finishDate, description);

        try {
            collectionOperations.addCollection(collectionToAdd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        new SwitchScene(actionEvent, "collections-view");
    }

    public void cancel(ActionEvent actionEvent) throws IOException {
        new SwitchScene(actionEvent, "collections-view");
    }
}
