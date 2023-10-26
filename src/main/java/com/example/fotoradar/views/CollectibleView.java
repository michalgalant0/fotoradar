package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectibleFormComponent;
import com.example.fotoradar.components.MiniGalleryComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

public class CollectibleView {
    @FXML
    private Label windowLabel;
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public CollectibleFormComponent collectibleFormComponent;

    @Setter
    private Collectible collectible = new Collectible();

    public void initialize() {
        System.out.println("CollectibleView.initialize: "+collectible);
        // ustawienie nagłówka okna
        setWindowLabel(collectible.getTitle());
        // wypełnienie komponentu z formularzem
        fillCollectibleForm();
    }

    private void setWindowLabel(String collectibleName) {
        windowLabel.setText("kolekcje/ <nazwa kolekcji nadrzędnej>/ "+collectibleName);
    }

    private void fillCollectibleForm() {
        collectibleFormComponent.setTitleTextField(collectible.getTitle());
        collectibleFormComponent.setStartDatePicker(collectible.getStartDate());
        collectibleFormComponent.setFinishDatePicker(collectible.getFinishDate());
        collectibleFormComponent.setDescriptionTextArea(collectible.getDescription());
        collectibleFormComponent.setStatusComboBox("do pobrania");
    }

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
