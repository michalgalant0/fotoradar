package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectibleFormComponent;
import com.example.fotoradar.components.MiniGalleryComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class CollectibleView {
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public CollectibleFormComponent collectibleFormComponent;

    @FXML
    private void saveCollectible(ActionEvent event) throws IOException {
        System.out.println("zapis obiektu");
        // pozostaje na tym samym widoku
    }

    @FXML
    private void addSketch(ActionEvent event) throws IOException {
        System.out.println("dodawanie szkicu");
        // otwarcie modułu do szkicowania
    }

    @FXML
    private void addPhoto(ActionEvent event) throws IOException {
        System.out.println("dodawanie zdjecia");
        new SwitchScene().displayWindow("AddPhotosWindow", "Dodaj zdjęcia");
    }

    @FXML
    private void manageSegments(ActionEvent event) throws IOException {
        System.out.println("przejscie do segmentow");
        new SwitchScene().switchScene(event, "segmentsView");
    }

    @FXML
    private void removeCollectible(ActionEvent event) throws IOException {
        System.out.println("usuwanie obiektu");
        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie");
    }

    @FXML
    private void backToCollection(ActionEvent event) throws IOException {
        System.out.println("powrot do kolekcji");
        new SwitchScene().switchScene(event, "collectionView");
    }
}
