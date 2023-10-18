package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.io.IOException;

@Setter
public class EditCollectionViewController {

    public TextField titleTextField;
    public TextField startDateTextField;
    public TextField finishDateTextField;
    public TextArea descriptionTextArea;

    public Button editCollectionButton;
    public Button cancelButton;

    private Collection collection;

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

        // powrót do kolekcji musi załadować tę samą kolekcję, która została załadowana tutaj w formularzu
        new SwitchScene(event, "collection_view", controller);
    }

    public void editCollection(ActionEvent event) {
    }
}
