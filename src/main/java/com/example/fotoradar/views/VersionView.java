package com.example.fotoradar.views;

import com.example.fotoradar.AddPhotoListener;
import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.VersionFormComponent;
import com.example.fotoradar.components.MiniGalleryComponent;
import com.example.fotoradar.databaseOperations.PhotoOperations;
import com.example.fotoradar.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VersionView implements AddPhotoListener {
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

    private String versionPhotosPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/SEGMENTY/%s/WERSJE/%s/";
    private PhotoOperations photoOperations;

    public void initialize() throws SQLException {
        photoOperations = new PhotoOperations();

        setWindowLabel();

        versionFormComponent.fillForm(version);

        versionPhotosPath = String.format(versionPhotosPath,
                System.getProperty("user.dir"), parentCollectionName, parentCollectible.getTitle(),
                parentSegment.getTitle(), version.getName());

        fillMiniGallery();

        new DirectoryOperator().createStructure(version, parentCollectionName, parentCollectible.getTitle(), parentSegment.getTitle());
    }

    private void fillMiniGallery() throws SQLException {
        miniGalleryComponent.setParentDirectory(String.format(versionPhotosPath));
        // konwersja listy photos na imagemodels
        ArrayList<Photo> photos = photoOperations.getAllPhotos(version.getId());
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        for (Photo photo : photos) {
            ImageModel imageModel = new ImageModel(photo.getId(), photo.getFileName(), photo.getParentId());
            imageModels.add(imageModel);
        }
        miniGalleryComponent.setImages(imageModels);

        System.out.println(miniGalleryComponent.parentDirectory);
        System.out.println(miniGalleryComponent.images);
        miniGalleryComponent.fillComponent();
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

    @Override
    public void onAddingPhotosFinished(List<File> selectedFiles) throws IOException, SQLException {

    }
}
