package com.example.fotoradar.views;

import com.example.fotoradar.*;
import com.example.fotoradar.components.ImageViewerComponent;
import com.example.fotoradar.components.SegmentFormComponent;
import com.example.fotoradar.databaseOperations.SegmentOperations;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.ImageModel;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Thumbnail;
import com.example.fotoradar.segmenter.Segmenter;
import com.example.fotoradar.segmenter.SegmenterListener;
import com.example.fotoradar.windows.AddPhotosWindow;
import com.example.fotoradar.windows.ConfirmDeletePopup;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SegmentsView implements SegmenterListener, AddPhotoListener, SegmentsListener, RemoveStructureListener {
    @FXML
    public Label windowLabel;
    @FXML
    public ImageViewerComponent imageViewerComponent;
    @FXML
    public SegmentFormComponent segmentFormComponent;

    @Setter
    private Collectible collectible;
    @Setter
    private String parentCollectionName;
    @Setter
    private ArrayList<Segment> segments;
    private Segment currentSegment;

    private Segmenter segmenter;
    private String collectibleThumbnailsPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/MINIATURY/";
    private ThumbnailOperations thumbnailOperations;
    private SegmentOperations segmentOperations;

    private int lastIndex;

    public SegmentsView() {
        try {
            thumbnailOperations = new ThumbnailOperations();
            segmentOperations = new SegmentOperations();
            collectible = new Collectible();
            segments = new ArrayList<>();
            lastIndex = 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() throws SQLException {
        System.out.println("SegmentsView.initialize: " + collectible);
        setWindowLabel(parentCollectionName, collectible.getTitle());
        collectibleThumbnailsPath = String.format(collectibleThumbnailsPath,
                System.getProperty("user.dir"), parentCollectionName, collectible.getTitle());

        imageViewerComponent.setForSegmentsView(true);
        ArrayList<ImageModel> imageModels = new ArrayList<>(getThumbnails());
        imageViewerComponent.setImages(imageModels);
        imageViewerComponent.setParentDirectory(collectibleThumbnailsPath);
        imageViewerComponent.initialize();
        imageViewerComponent.setSegmentsListener(this);

        segments = segmentOperations.getAllSegments(collectible.getId());
        lastIndex = segments.size();
        for (Segment segment : segments)
            if (segment.getTitle() != null && !segment.getTitle().isBlank() && !segment.getTitle().isEmpty())
                new DirectoryOperator().createStructure(segment, parentCollectionName, collectible.getTitle());
    }

    private ArrayList<Thumbnail> getThumbnails() throws SQLException {
        ArrayList<Thumbnail> imageFiles = new ArrayList<>();

        imageFiles.addAll(thumbnailOperations.getAllThumbnails(collectible.getId()));

        return imageFiles;
    }

    private void setWindowLabel(String parentCollectionName, String collectibleName) {
        windowLabel.setText(
                String.format("kolekcje/ %s/ %s/ segmenty",
                        parentCollectionName, collectibleName)
        );
    }

    @FXML
    private void saveSegment() throws SQLException {
        System.out.println("zapis segmentu");
        Segment segmentToUpdate = currentSegment;
        String oldPath = String.format("%s/KOLEKCJE/%s/OBIEKTY/%s/SEGMENTY/%s",
                System.getProperty("user.dir"), parentCollectionName, collectible.getTitle(), currentSegment.getTitle());

        // pobranie nowych danych z formularza
        currentSegment.setTitle(segmentFormComponent.nameTextField.getText());
        currentSegment.setStartDate(String.valueOf(segmentFormComponent.startDatePicker.getValue()));
        currentSegment.setFinishDate(String.valueOf(segmentFormComponent.finishDatePicker.getValue()));
        currentSegment.setDescription(segmentFormComponent.descriptionTextArea.getText());
        currentSegment.setStatusId(1); // test

        // aktualizacja danych w bazie
        segmentOperations.updateSegment(segmentToUpdate);
        // aktualizacja nazwy katalogu
        String newName = currentSegment.getTitle();
        if (new File(oldPath).exists())
            DirectoryOperator.getInstance().updateDirectoryName(oldPath, newName);
        else
            DirectoryOperator.getInstance().createStructure(segmentToUpdate, parentCollectionName, collectible.getTitle());
    }

    @FXML
    private void deleteSegment(ActionEvent event) throws IOException {
        System.out.println("usuwanie segmentu");

        ConfirmDeletePopup confirmDeletePopup = new ConfirmDeletePopup();
        confirmDeletePopup.setRemoveStructureListener(this);
        confirmDeletePopup.setSourceEvent(event);
        confirmDeletePopup.setParentView(this);

        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie", confirmDeletePopup);
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        //odswiezenie widoku
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public void onSegmentationFinished(ArrayList<Segmenter.Segment> segments, int segmentedThumbnailId) throws SQLException {
        System.out.println("SegmentsView.onSegmentationFinished: segmentsFromSegmenter "+segments);
        segmentOperations = new SegmentOperations();
        String tmpName = "segment%d";
        for (Segmenter.Segment segment : segments) {
            // utworzenie obiektu, ktory bedzie dodawany
            Segment segmentToAdd = new Segment(String.format(tmpName, lastIndex+=1), segment.toString(), collectible.getId(), segmentedThumbnailId);
            // dodanie do bazy
            segmentOperations.addSegment(segmentToAdd);
            // utworzenie katalogow
            DirectoryOperator.getInstance().createStructure(segmentToAdd, parentCollectionName, collectible.getTitle());
        }

        //odswiezenie widoku
        refresh();
    }

    @Override
    public void onAddingPhotosFinished(List<File> selectedFiles) throws IOException, SQLException {
        // wyswietlenie listy zdjec w konsoli
        System.out.println("SegmentsView.onAddingPhotosFinished: selectedFilesFromAddPhotosWindow "+selectedFiles);
        for (File file : selectedFiles) {
            // przekopiowanie wybranych plikow do utworzonej struktury aplikacji
            String destinationFilePath = collectibleThumbnailsPath+file.getName();
            // kopiowanie dla potrzeb testowych - domyślnie przenoszenie
            Files.copy(
                    file.toPath(), Path.of(destinationFilePath),
                    StandardCopyOption.REPLACE_EXISTING
            );
            // dodanie miniatur do bazy
            thumbnailOperations.addThumbnail(
                    new Thumbnail(file.getName(), collectible.getId())
            );
        }

        //odswiezenie widoku
        refresh();
    }

    @Override
    public void onCurrentSegmentChanged(Segment segment) {
        System.out.println("SegmentsView.onCurrentSegmentChanged: "+segment);
        currentSegment = segment;

        segmentFormComponent.setSegment(currentSegment);
        segmentFormComponent.setParentCollectionName(parentCollectionName);
        segmentFormComponent.setParentCollectible(collectible);
        try {
            segmentFormComponent.fillVersionComboBox();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void passCurrentImageToSegmenter() {
        Image currentImage = imageViewerComponent.getCurrentImage();
        segmenter.setCurrentImage(currentImage);
        int currentThumbnailId = imageViewerComponent.getCurrentThumbnail().getId();// pobranie id miniatury na podstawie nazwy pliku zaladowanego obrazka
        segmenter.setCurrentThumbnailId(currentThumbnailId);
    }

    @FXML
    private void backToCollectible(ActionEvent event) throws IOException {
        System.out.println("powrót do obiektu");
        CollectibleView collectibleView = new CollectibleView();
        collectibleView.setCollectible(collectible);
        new SwitchScene().switchScene(event, "collectibleView", collectibleView);
    }

    @Override
    public void onDeleteConfirmed(ActionEvent event, Object view) {
        System.out.println("SegmentsView.onDeleteConfirmed: "+currentSegment);

        // usuwanie z bazy
        try {
            if (segmentOperations.deleteSegmentWithSubstructures(currentSegment.getId()))
                System.out.println("usunieto segment z bazy");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //usuwanie katalogóœ
        DirectoryOperator.getInstance().removeStructure(currentSegment, parentCollectionName, collectible.getTitle());

        // odświeżenie widoku
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refresh() throws SQLException {
        setWindowLabel(parentCollectionName, collectible.getTitle());
        collectibleThumbnailsPath = String.format(collectibleThumbnailsPath,
                System.getProperty("user.dir"), parentCollectionName, collectible.getTitle());

        imageViewerComponent.setForSegmentsView(true);
        ArrayList<ImageModel> imageModels = new ArrayList<>(getThumbnails());
        imageViewerComponent.setImages(imageModels);
        imageViewerComponent.setParentDirectory(collectibleThumbnailsPath);
        imageViewerComponent.initialize();
        imageViewerComponent.setSegmentsListener(this);

        segments = segmentOperations.getAllSegments(collectible.getId());
        lastIndex = segments.size();
    }

    //todo dodac odswiezanie na dodaniu wersji i zaladowac pierwszą jako default
}
