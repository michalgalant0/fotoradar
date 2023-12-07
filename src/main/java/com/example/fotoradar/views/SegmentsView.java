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
import com.example.fotoradar.painter.Painter;
import com.example.fotoradar.segmenter.Segmenter;
import com.example.fotoradar.segmenter.SegmenterListener;
import com.example.fotoradar.windows.AddPhotosWindow;
import com.example.fotoradar.windows.ConfirmDeletePopup;
import com.example.fotoradar.windows.OnWindowClosedListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SegmentsView implements SegmenterListener, AddPhotoListener, SegmentsListener, RemoveStructureListener, OnWindowClosedListener {
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
    private String collectibleThumbnailsPath = Paths.get("%s","KOLEKCJE","%s","OBIEKTY","%s","MINIATURY").toString();
    private ThumbnailOperations thumbnailOperations;
    private SegmentOperations segmentOperations;
    private Painter painter;

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
        //"schowanie" formularza segmentFormComponent
        segmentFormComponent.setVisible(false);

        thumbnailOperations = new ThumbnailOperations();
        segmentOperations = new SegmentOperations();

        System.out.println("SegmentsView.initialize: " + collectible);
        setWindowLabel(parentCollectionName, collectible.getTitle());
        collectibleThumbnailsPath = String.format(collectibleThumbnailsPath,
                Main.getDefPath(), parentCollectionName, collectible.getTitle());

        imageViewerComponent.setImageViewerFlag(ImageViewerFlag.SEGMENTS);
        ArrayList<ImageModel> imageModels = new ArrayList<>(getThumbnails());
        imageViewerComponent.setImages(imageModels);
        imageViewerComponent.setParentDirectory(collectibleThumbnailsPath);
        imageViewerComponent.setSegmentFormComponent(segmentFormComponent);
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
        String oldPath = String.format(Paths.get("%s","KOLEKCJE","%s","OBIEKTY","%s","SEGMENTY","%s").toString(),
                Main.getDefPath(), parentCollectionName, collectible.getTitle(), currentSegment.getTitle());

        // pobranie nowych danych z formularza
        currentSegment.setTitle(segmentFormComponent.nameTextField.getText());
        currentSegment.setStartDate(String.valueOf(segmentFormComponent.startDatePicker.getValue()));
        currentSegment.setFinishDate(String.valueOf(segmentFormComponent.finishDatePicker.getValue()));
        currentSegment.setDescription(segmentFormComponent.descriptionTextArea.getText());
        currentSegment.setStatus(segmentFormComponent.statusComboBox.getValue()); // test

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
    private void addSketch() throws Exception {
        System.out.println("dodanie szkicu");
        Painter painter = new Painter();
        painter.setParentCollectionName(parentCollectionName);
        painter.setCollectible(collectible);
        painter.setOnWindowClosedListener(this);
        Stage stage = new Stage();
        painter.start(stage);
    }

    @FXML
    private void addPhoto() throws IOException {
        System.out.println("dodanie zdjęcia");

        AddPhotosWindow addPhotosWindow = new AddPhotosWindow();
        addPhotosWindow.setAddPhotoListener(this);
        addPhotosWindow.setOnWindowClosedListener(this);

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
        // przekazanie gotowej do wyświetlenia listy segmentów do segmentera
        segmenter.setSegmentsToShow(getSegmenterSegmentsForCurrentThumbnail());

        segmenter.setSegmenterListener(this);
        Stage stage = new Stage();
        segmenter.start(stage);
    }

    private ArrayList<Segmenter.Segment> getSegmenterSegmentsForCurrentThumbnail() {
        ArrayList<Segmenter.Segment> segmenterSegments = new ArrayList<>();

        ArrayList<Segment> segmentsToConvert = segments.stream()
                .filter(segment -> segment.getThumbnailId() == imageViewerComponent.getCurrentThumbnail().getId())
                .collect(Collectors.toCollection(ArrayList::new));

        for(Segment segment : segmentsToConvert)
            segmenterSegments.add(segment.toSegmenterSegment());

        return segmenterSegments;
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
        System.out.println("SegmentsView.onAddingPhotosFinished: selectedFilesFromAddPhotosWindow " + selectedFiles);

        for (File file : selectedFiles) {
            // Wczytaj oryginalne zdjęcie
            BufferedImage originalImage = ImageIO.read(file);

            // Oblicz proporcje skalowania
            double scaleFactor = 1.0;
            int targetWidth = 800;
            int targetHeight = 800;

            if (originalImage.getWidth() > originalImage.getHeight()) {
                scaleFactor = (double) targetWidth / originalImage.getWidth();
            } else {
                scaleFactor = (double) targetHeight / originalImage.getHeight();
            }

            // Przeskaluj obraz
            int scaledWidth = (int) (originalImage.getWidth() * scaleFactor);
            int scaledHeight = (int) (originalImage.getHeight() * scaleFactor);
            java.awt.Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, java.awt.Image.SCALE_SMOOTH);
            BufferedImage scaledBufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            scaledBufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

            // Utwórz ścieżkę docelową
            String destinationFilePath = collectibleThumbnailsPath + file.getName();

            // Zapisz przeskalowany obraz do pliku
            ImageIO.write(scaledBufferedImage, "jpg", new File(destinationFilePath));

            // Dodaj miniaturę do bazy
            thumbnailOperations.addThumbnail(new Thumbnail(file.getName(), collectible.getId()));
        }

        // Odśwież widok
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
                Main.getDefPath(), parentCollectionName, collectible.getTitle());

        imageViewerComponent.setImageViewerFlag(ImageViewerFlag.SEGMENTS);
        ArrayList<ImageModel> imageModels = new ArrayList<>(getThumbnails());
        imageViewerComponent.setImages(imageModels);
        imageViewerComponent.setParentDirectory(collectibleThumbnailsPath);
        imageViewerComponent.initialize();
        imageViewerComponent.setSegmentsListener(this);

        segments = segmentOperations.getAllSegments(collectible.getId());
        lastIndex = segments.size();
    }

    @Override
    public void onWindowClosed() {
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
