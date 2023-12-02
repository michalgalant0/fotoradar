package com.example.fotoradar.views;

import com.example.fotoradar.AddPhotoListener;
import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.RemoveStructureListener;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.MiniGalleryComponent;
import com.example.fotoradar.components.VersionFormComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.databaseOperations.PhotoOperations;
import com.example.fotoradar.databaseOperations.VersionOperations;
import com.example.fotoradar.models.*;
import com.example.fotoradar.windows.AddPhotosWindow;
import com.example.fotoradar.windows.ConfirmDeletePopup;
import com.example.fotoradar.windows.OnWindowClosedListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VersionView implements AddPhotoListener, RemoveStructureListener, OnWindowClosedListener {
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

    private String versionPhotosPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/SEGMENTY/%s/WERSJE/%s";
    private PhotoOperations photoOperations;

    public VersionView() {
        try {
            photoOperations = new PhotoOperations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() throws SQLException {
        setWindowLabel();
        versionFormComponent.setParentCollectionId(parentCollectible.getParentCollectionId());
        versionFormComponent.setOnWindowClosedListener(this);
        versionFormComponent.fillForm(version);
        System.out.println("VersionView: "+version);
        versionFormComponent.fillTeamComboBox();
        versionPhotosPath = Paths.get(
                System.getProperty("user.dir"),"KOLEKCJE",
                parentCollectionName, "OBIEKTY", parentCollectible.getTitle(),
                "SEGMENTY", parentSegment.getTitle(), "WERSJE", version.getName()
        ).toString();
        fillMiniGallery();

        DirectoryOperator.getInstance().createStructure(version, parentCollectionName, parentCollectible.getTitle(), parentSegment.getTitle());
    }

    private void fillMiniGallery() throws SQLException {
        miniGalleryComponent.setOnWindowClosedListener(this);
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
        String oldPath = versionPhotosPath;
        version.setName(versionFormComponent.nameTextField.getText());
        version.setStartDate(versionFormComponent.startDatePicker.getValue().toString());
        version.setFinishDate(versionFormComponent.finishDatePicker.getValue().toString());
        version.setDescription(versionFormComponent.descriptionTextArea.getText());

        // update obiektu do bazy
        VersionOperations versionOperations = new VersionOperations();
        versionOperations.updateVersion(version);
        System.out.println("zapis wersji");

        // aktualizacja katalogow
        String newName = version.getName();
        DirectoryOperator.getInstance().updateDirectoryName(oldPath, newName);

        setVersion(version);
        refresh();
    }

    @FXML
    private void addPhotos(ActionEvent event) throws IOException {
        System.out.println("dodawanie zdjęć");

        AddPhotosWindow addPhotosWindow = new AddPhotosWindow();
        addPhotosWindow.setAddPhotoListener(this);
        addPhotosWindow.setOnWindowClosedListener(this);

        new SwitchScene().displayWindow("AddPhotosWindow", "Dodaj zdjęcia", addPhotosWindow);
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void manageTeams(ActionEvent event) throws SQLException, IOException {
        System.out.println("przejscie do zespołów");
        TeamsView teamsView = new TeamsView();
        teamsView.setParentView(this);
        teamsView.setParentCollection(new CollectionOperations().getCollectionById(parentCollectible.getParentCollectionId()));
        new SwitchScene().switchScene(event, "teamsView", teamsView);
    }

    @FXML
    private void removeVersion(ActionEvent event) throws IOException {
        System.out.println("usuwanie wersji");

        ConfirmDeletePopup confirmDeletePopup = new ConfirmDeletePopup();
        confirmDeletePopup.setRemoveStructureListener(this);
        confirmDeletePopup.setSourceEvent(event);
        // widok nadrzedny do powrotu
        SegmentsView segmentsView = new SegmentsView();
        segmentsView.setCollectible(parentCollectible);
        segmentsView.setParentCollectionName(parentCollectionName);
        confirmDeletePopup.setParentView(segmentsView);

        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie", confirmDeletePopup);
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
            String destinationFilePath = Paths.get(versionPhotosPath, file.getName()).toString();
            // kopiowanie dla potrzeb testowych - domyślnie przenoszenie
            Files.copy(
                    file.toPath(), Path.of(destinationFilePath),
                    StandardCopyOption.REPLACE_EXISTING
            );
            float fileSize = (float) file.length() /(1024*1024);
            // dodanie zdjęć do bazy
            photoOperations.addPhoto(
                    new Photo(file.getName(), version.getId(), fileSize)
            );
        }
        refresh();
    }

    @Override
    public void onDeleteConfirmed(ActionEvent event, Object view) {
        System.out.println("VersionView.onDeleteConfirmed: "+version);
        // usuwanie z bazy
        try {
            if (new VersionOperations().deleteVersionWithSubstructures(version.getId()))
                System.out.println("usunieto wersje z bazy");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //usuwanie katalogow
        DirectoryOperator.getInstance().removeStructure(version, parentCollectionName, parentCollectible.getTitle(), parentSegment.getTitle());

        // Spróbuj odświeżyć scenę główną
        try {
            new SwitchScene().switchScene(event, "segmentsView", view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refresh() throws SQLException {
        setWindowLabel();
        versionFormComponent.fillForm(version); //todo dodać pobieranie wersji na odświeżeniu dla danego segmentu, żeby nie musieć klikać dwa razy
        versionFormComponent.fillTeamComboBox();
        versionPhotosPath = Paths.get(
                System.getProperty("user.dir"),"KOLEKCJE",
                parentCollectionName, "OBIEKTY", parentCollectible.getTitle(),
                "SEGMENTY", parentSegment.getTitle(), "WERSJE", version.getName()
        ).toString();
        fillMiniGallery();
    }

    @Override
    public void onWindowClosed() {
        System.err.println("REFRESH NA VERSION VIEW");
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
