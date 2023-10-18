package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EditCollectionViewController {
    public Button editCollectionButton;
    public Button cancelButton;
    public TextField titleTextField;
    public TextField startDateTextField;
    public TextField finishDateTextField;
    public TextArea descriptionTextArea;

    private Collection collection;

    public void cancel(ActionEvent event) throws IOException {
        CollectionViewController controller = new CollectionViewController();
        controller.setCollection(collection);

        // powrót do kolekcji musi załadować tę samą kolekcję, która została załadowana tutaj w formularzu
        new SwitchScene(event, "collection_view", controller);
    }

    public void editCollection(ActionEvent event) {
    }
}
