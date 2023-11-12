package com.example.fotoradar.controllers.views;

import com.example.fotoradar.listeners.AddPhotoListener;
import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.controllers.components.VersionFormComponent;
import com.example.fotoradar.controllers.components.MiniGalleryComponent;
import com.example.fotoradar.databaseOperations.PhotoOperations;
import com.example.fotoradar.databaseOperations.VersionOperations;
import com.example.fotoradar.models.*;
import com.example.fotoradar.controllers.windows.AddPhotosWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
    private void saveVersion() throws SQLException {

        version.setName(versionFormComponent.nameTextField.getText());
        version.setStartDate(versionFormComponent.startDatePicker.getValue().toString());
        version.setFinishDate(versionFormComponent.finishDatePicker.getValue().toString());
        version.setDescription(versionFormComponent.descriptionTextArea.getText());

        // update obiektu do bazy
        VersionOperations versionOperations = new VersionOperations();
        versionOperations.updateVersion(version);
        System.out.println("zapis wersji");

    }

    @FXML
    private void addPhotos(ActionEvent event) throws IOException {
        System.out.println("dodawanie zdjęć");

        AddPhotosWindow addPhotosWindow = new AddPhotosWindow();
        addPhotosWindow.setAddPhotoListener(this);

        new SwitchScene().displayWindow("AddPhotosWindow", "Dodaj zdjęcia", addPhotosWindow);
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
        // wyswietlenie listy zdjec w konsoli
        System.out.println("VersionView.onAddingPhotosFinished: selectedFilesFromAddPhotosWindow "+selectedFiles);
        for (File file : selectedFiles) {
            // przekopiowanie wybranych plikow do utworzonej struktury aplikacji
            String destinationFilePath = versionPhotosPath+file.getName();
            // kopiowanie dla potrzeb testowych - domyślnie przenoszenie
            Files.copy(
                    file.toPath(), Path.of(destinationFilePath),
                    StandardCopyOption.REPLACE_EXISTING
            );
            // dodanie zdjęć do bazy
            photoOperations.addPhoto(
                    new Photo(file.getName(), version.getId())
            );
        }
    }
}