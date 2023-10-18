package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.CollectionOperations;
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
public class EditCollectionViewController {

    public TextField titleTextField;
    public TextField startDateTextField;
    public TextField finishDateTextField;
    public TextArea descriptionTextArea;

    public Button editCollectionButton;
    public Button cancelButton;

    private Collection collection;

    private final CollectionOperations collectionOperations = new CollectionOperations();

    public EditCollectionViewController() throws SQLException {
    }

    @FXML
    public void initialize() {
        fillUpFields();
    }

    private void fillUpFields () {
        // pobranie danych z przekazanej kolekcji
        String title = this.collection.getTitle();
        String startDate = this.collection.getStartDate();
        String finishDate = this.collection.getFinishDate();
        String description = this.collection.getDescription();

        // wypełnienie formularza przekazanymi danymi
        titleTextField.setText(title);
        startDateTextField.setText(startDate);
        finishDateTextField.setText(finishDate);
        descriptionTextArea.setText(description);
    }

    public void cancel(ActionEvent event) throws IOException {
        CollectionViewController controller = new CollectionViewController();
        controller.setCollection(collection);
        new SwitchScene(event, "collection_view", controller);
    }

    public void editCollection(ActionEvent event) throws IOException {
        String newTitle = titleTextField.getText();
        String newStartDate = startDateTextField.getText();
        String newFinishDate = finishDateTextField.getText();
        String newDescription = descriptionTextArea.getText();

        this.collection.setTitle(newTitle);
        this.collection.setStartDate(newStartDate);
        this.collection.setFinishDate(newFinishDate);
        this.collection.setDescription(newDescription);

        System.out.println(this.collection);
        try {
            collectionOperations.updateCollection(this.collection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        CollectionViewController controller = new CollectionViewController();
        controller.setCollection(collection);
        new SwitchScene(event, "collection_view", controller);
    }
}
