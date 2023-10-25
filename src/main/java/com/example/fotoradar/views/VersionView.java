package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.VersionFormComponent;
import com.example.fotoradar.components.MiniGalleryComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

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
    private void addPhotos(ActionEvent event) throws IOException {
        System.out.println("dodawanie zdjęć");
        new SwitchScene().displayWindow("AddPhotosWindow", "Dodaj zdjęcia");
    }

    @FXML
    private void manageTeams() {
        System.out.println("przejscie do zespołów");

    }

    @FXML
    private void removeVersion(ActionEvent event) throws IOException {
        System.out.println("usuwanie wersji");
        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie");
    }

    @FXML
    private void backToSegments(ActionEvent event) throws IOException {
        System.out.println("powrot do segmentów");
        new SwitchScene().switchScene(event, "segmentsView");

    }
}
