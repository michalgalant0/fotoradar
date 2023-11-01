package com.example.fotoradar.views;

import com.example.fotoradar.AddPhotoListener;
import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.ImageViewerComponent;
import com.example.fotoradar.components.SegmentFormComponent;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.segmenter.Segmenter;
import com.example.fotoradar.segmenter.SegmenterListener;
import com.example.fotoradar.windows.AddPhotosWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class SegmentsView implements SegmenterListener, AddPhotoListener {
    @FXML
    public Label windowLabel;
    @FXML
    public ImageViewerComponent imageViewerComponent;
    @FXML
    public SegmentFormComponent segmentFormComponent;

    @Setter
    private Collectible collectible = new Collectible();
    @Setter
    private String parentCollectionName;
    @Setter
    private ArrayList<Segment> segments = new ArrayList<>();

    private Segmenter segmenter;
    private String collectibleThumbnailsPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/MINIATURY/";

    public void initialize() {
        System.out.println("SegmentsView.initialize: " + collectible);
        setWindowLabel(parentCollectionName, collectible.getTitle());
        imageViewerComponent.setForSegmentsView(true);

        collectibleThumbnailsPath = String.format(collectibleThumbnailsPath,
                System.getProperty("user.dir"), parentCollectionName, collectible.getTitle());

        for (Segment segment : segments) {
            new DirectoryOperator().createStructure(segment, parentCollectionName, collectible.getTitle());
        }
    }

    private void setWindowLabel(String parentCollectionName, String collectibleName) {
        windowLabel.setText(
                String.format("kolekcje/ %s/ %s/ segmenty",
                        parentCollectionName, collectibleName)
        );
    }

    @FXML
    private void saveSegment() {
        System.out.println("zapis segmentu");
    }

    @FXML
    private void deleteSegment() throws IOException {
        System.out.println("usuwanie segmentu");
        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie");
    }

    @FXML
    private void addSketch() {
        System.out.println("dodanie szkicu");
    }

    @FXML
    private void addPhoto() throws IOException {
        System.out.println("dodanie zdjęcia");

        AddPhotosWindow addPhotosWindow = new AddPhotosWindow();
        addPhotosWindow.setAddPhotoListener(this);

        new SwitchScene().displayWindow("AddPhotosWindow", "Dodaj miniatury", addPhotosWindow);
    }

    @FXML
    private void addSegments() {
        System.out.println("dodanie segmentu");

        // Start the Segmenter
        segmenter = new Segmenter();
        // przekazanie bieżącego zdjęcia do segmentera
        passCurrentImageToSegmenter();

        segmenter.setSegmenterListener(this);
        Stage stage = new Stage();
        segmenter.start(stage);
    }

    @Override
    public void onSegmentationFinished(ArrayList<Segmenter.Segment> segments) {
        System.out.println("SegmentsView.onSegmentationFinished: segmentsFromSegmenter "+segments);
    }

    @Override
    public void onAddingPhotosFinished(List<File> selectedFiles) throws IOException {
        System.out.println("SegmentsView.onAddingPhotosFinished: selectedFilesFromAddPhotosWindow "+selectedFiles);
        for (File file : selectedFiles) {
            // kopiowanie dla potrzeb testowych - domyślnie przenoszenie
            Files.copy(
                    file.toPath(), Path.of(collectibleThumbnailsPath+file.getName()),
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    private void passCurrentImageToSegmenter() {
        Image currentImage = imageViewerComponent.getCurrentImage();
        segmenter.setCurrentImage(currentImage);
    }

    @FXML
    private void backToCollectible(ActionEvent event) throws IOException {
        System.out.println("powrót do obiektu");
        CollectibleView collectibleView = new CollectibleView();
        collectibleView.setCollectible(collectible);
        new SwitchScene().switchScene(event, "collectibleView", collectibleView);
    }
}
