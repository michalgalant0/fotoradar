package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
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

    public void cancel(ActionEvent event) throws IOException {
        // powrót do kolekcji musi załadować tę samą kolekcję, która została załadowana tutaj w formularzu
        new SwitchScene(event, "collection_view");
    }

    public void editCollection(ActionEvent event) {
    }
}
