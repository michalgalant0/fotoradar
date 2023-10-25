package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.MiniGalleryComponent;
import com.example.fotoradar.components.SegmentFormComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import java.io.IOException;

public class SegmentsView {
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public SegmentFormComponent segmentFormComponent;

    @FXML
    private void saveSegment(){
        System.out.println("zapis segmentu");
    }
    @FXML
    private void deleteSegment(){
        System.out.println("usuwanie segmentu");
    }
    @FXML
    private void addSketch(){
        System.out.println("dodanie szkicu");
    }
    @FXML
    private void addPhoto() throws IOException {
        System.out.println("dodanie zdjęcia");
        new SwitchScene().displayWindow("AddPhotosWindow", "Dodaj zdjęcie");
    }
    @FXML
    private void addSegments(){
        System.out.println("dodanie segmentu");
    }
    @FXML
    private void backToCollection(ActionEvent event) throws IOException{
        System.out.println("powrót do kolekcji");
        new SwitchScene().switchScene(event, "collectionView");
    }
}
