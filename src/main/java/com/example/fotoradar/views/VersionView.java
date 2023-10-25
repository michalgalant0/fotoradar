package com.example.fotoradar.views;

import com.example.fotoradar.components.VersionFormComponent;
import com.example.fotoradar.components.MiniGalleryComponent;
import javafx.fxml.FXML;

public class VersionView {
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public VersionFormComponent versionFormComponent;

    @FXML
    private void saveVersion() {
        System.out.println("zapis wersji");
    }

    @FXML
    private void addPhotos() {
        System.out.println("dodawanie zdjęć");
    }

    @FXML
    private void manageTeams() {
        System.out.println("przejscie do zespołów");
    }

    @FXML
    private void removeVersion() {
        System.out.println("usuwanie wersji");
    }

    @FXML
    private void backToSegments() {
        System.out.println("powrot do segmentów");
    }
}
