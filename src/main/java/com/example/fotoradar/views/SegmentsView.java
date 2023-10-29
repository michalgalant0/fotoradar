package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.MiniGalleryComponent;
import com.example.fotoradar.components.SegmentFormComponent;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Segment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;


import java.io.IOException;
import java.util.ArrayList;

public class SegmentsView {
    @FXML
    public Label windowLabel;
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public SegmentFormComponent segmentFormComponent;

    @Setter
    private Collectible collectible = new Collectible();
    @Setter
    private String parentCollectionName = "";
    private ArrayList<Segment> segments = new ArrayList<>();

    public void initialize() {
        System.out.println("SegmentsView.initialize: "+collectible);
        setWindowLabel(parentCollectionName, collectible.getTitle());
    }

    private void setWindowLabel(String parentCollectionName, String collectibleName) {
        windowLabel.setText(
                String.format("kolekcje/ %s/ %s/ segmenty",
                        parentCollectionName, collectibleName)
        );
    }

    @FXML
    private void saveSegment(){
        System.out.println("zapis segmentu");
    }
    @FXML
    private void deleteSegment() throws IOException {
        System.out.println("usuwanie segmentu");
        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie");
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
    private void backToCollectible(ActionEvent event) throws IOException{
        System.out.println("powrót do obiektu");
        CollectibleView collectibleView = new CollectibleView();
        collectibleView.setCollectible(collectible);
        new SwitchScene().switchScene(event, "collectibleView", collectibleView);
    }
}
