package com.example.fotoradar.views;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.VersionFormComponent;
import com.example.fotoradar.components.MiniGalleryComponent;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Version;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;

public class VersionView {
    @FXML
    public Label windowLabel;
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public VersionFormComponent versionFormComponent;

    @Setter
    private String parentCollectionName;
    @Setter
    private Collectible parentCollectible;
    @Setter
    private Segment parentSegment;
    @Setter
    private Version version;

    public void initialize() {
        setWindowLabel();
        new DirectoryOperator().createStructure(version, parentCollectionName, parentCollectible.getTitle(), parentSegment.getTitle());
    }

    private void setWindowLabel() {
        windowLabel.setText(
                String.format("kolekcje/ %s/ %s/ %s/ %s",
                        parentCollectionName, parentCollectible.getTitle(), parentSegment.getTitle(), version.getName())
        );
    }

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
        SegmentsView segmentsView = new SegmentsView();
        segmentsView.setCollectible(parentCollectible);
        segmentsView.setParentCollectionName(parentCollectionName);
        new SwitchScene().switchScene(event, "segmentsView", segmentsView);

    }
}
