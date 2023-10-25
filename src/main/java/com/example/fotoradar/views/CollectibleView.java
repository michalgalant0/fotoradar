package com.example.fotoradar.views;

import com.example.fotoradar.components.CollectibleFormComponent;
import com.example.fotoradar.components.MiniGalleryComponent;
import javafx.fxml.FXML;

public class CollectibleView {
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public CollectibleFormComponent collectibleFormComponent;

    @FXML
    private void saveCollectible() {
        System.out.println("zapis obiektu");
    }

    @FXML
    private void addSketch() {
        System.out.println("dodawanie szkicu");
    }

    @FXML
    private void addPhoto() {
        System.out.println("dodawanie zdjecia");
    }

    @FXML
    private void manageSegments() {
        System.out.println("przejscie do segmentow");
    }

    @FXML
    private void removeCollectible() {
        System.out.println("usuwanie obiektu");
    }

    @FXML
    private void backToCollection() {
        System.out.println("powrot do kolekcji");
    }
}
