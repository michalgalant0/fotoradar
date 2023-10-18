package com.example.fotoradar.controllers;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

@Getter @Setter
public class CollectionViewController {
    public Label titleLabel;
    public Button backToCollectionsButton;
    public TextField titleTextField;
    public TextField startDateTextField;
    public TextField finishDateTextField;
    public TextArea descriptionTextArea;
    public ListView collectiblesListView;
    public Label collectiblesLabel;
    public Button saveCollectionButton;
    public Button addCollectibleButton;

    Collection collection;

    public CollectionViewController(Collection collection) {
        this.collection = collection;
    }

    @FXML
    public void initialize() {
        titleTextField.setText("zaladowany tytul "+collection.getTitle());
    }

    private void backToCollection (ActionEvent event) {

    }

    private void saveCollection (ActionEvent event) {

    }

    private void addCollectible (ActionEvent event) {

    }
}
