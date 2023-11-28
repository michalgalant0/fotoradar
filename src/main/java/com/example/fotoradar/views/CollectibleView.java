package com.example.fotoradar.views;

import com.example.fotoradar.AddPhotoListener;
import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.RemoveStructureListener;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectibleFormComponent;
import com.example.fotoradar.components.MiniGalleryComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.ImageModel;
import com.example.fotoradar.models.Thumbnail;
import com.example.fotoradar.painter.Painter;
import com.example.fotoradar.windows.AddPhotosWindow;
import com.example.fotoradar.windows.ConfirmDeletePopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

public class CollectibleView implements AddPhotoListener, RemoveStructureListener {
    @FXML
    private Label windowLabel;
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public CollectibleFormComponent collectibleFormComponent;

    @Setter
    private Collectible collectible = new Collectible();
    @Setter
    private Collection parentCollection = new Collection();
    private Painter painter;

    private String collectibleThumbnailsPath = "%s/KOLEKCJE/%s/OBIEKTY/%s/MINIATURY/";
    private ThumbnailOperations thumbnailOperations;

    public void initialize() throws SQLException {
        thumbnailOperations = new ThumbnailOperations();

        System.out.println("CollectibleView.initialize: "+collectible);
        // ustawienie kolekcji nadrzędnej
        setParentCollection();
        // ustawienie nagłówka okna
        setWindowLabel(parentCollection.getTitle(), collectible.getTitle());
        // wypełnienie komponentu z formularzem
        fillCollectibleForm();

        // wypelnienie komponentu miniGallery miniaturami
        fillMiniGallery();

        // ustawienie katalogu miniatur dla bieżącego obiektu
        collectibleThumbnailsPath = String.format(collectibleThumbnailsPath,
                System.getProperty("user.dir"), parentCollection.getTitle(), collectible.getTitle());

        new DirectoryOperator().createStructure(collectible, parentCollection.getTitle());
    }

    private void setParentCollection() throws SQLException {
        this.parentCollection = new CollectionOperations().getCollectionById(collectible.getParentCollectionId());
    }

    private void setWindowLabel(String collectionName, String collectibleName) {
        windowLabel.setText(String.format("kolekcje/ %s/ %s", collectionName, collectibleName));
    }

    private void fillCollectibleForm() {
        collectibleFormComponent.setTitleTextField(collectible.getTitle());
        collectibleFormComponent.setStartDatePicker(collectible.getStartDate());
        collectibleFormComponent.setFinishDatePicker(collectible.getFinishDate());
        collectibleFormComponent.setDescriptionTextArea(collectible.getDescription());
        collectibleFormComponent.setStatusComboBox("do pobrania");
    }

    @FXML
    private void saveCollectible(ActionEvent event) throws IOException, SQLException {

        collectible.setTitle(collectibleFormComponent.titleTextField.getText());
        collectible.setStartDate(collectibleFormComponent.startDatePicker.getValue().toString());
        collectible.setFinishDate(collectibleFormComponent.finishDatePicker.getValue().toString());
        collectible.setDescription(collectibleFormComponent.descriptionTextArea.getText());

        // update obiektu do bazy
        CollectibleOperations collectibleOperations = new CollectibleOperations();
        collectibleOperations.updateCollectible(collectible);

        System.out.println("zapis obiektu");
        // pozostaje na tym samym widoku
    }

    @FXML
    private void addSketch(ActionEvent event) throws Exception {
        System.out.println("dodawanie szkicu");
        // otwarcie modułu do szkicowania
        painter = new Painter();
        painter.setParentCollectionName(parentCollection.getTitle());
        painter.setCollectible(collectible);
        Stage stage = new Stage();
        painter.start(stage);
    }

    @FXML
    private void addPhoto(ActionEvent event) throws IOException {
        System.out.println("dodawanie zdjęcia");

        AddPhotosWindow addPhotosWindow = new AddPhotosWindow();
        addPhotosWindow.setAddPhotoListener(this);

        new SwitchScene().displayWindow("AddPhotosWindow", "Dodaj miniatury", addPhotosWindow);
    }

    @FXML
    private void manageSegments(ActionEvent event) throws IOException {
        System.out.println("przejscie do segmentow");
        System.out.println("CollectibleView.goToSegments: "+collectible);

        // utworzenie kontrolera widoku docelowego i ustawienie jego pól
        SegmentsView segmentsView = new SegmentsView();
        segmentsView.setCollectible(collectible);
        segmentsView.setParentCollectionName(parentCollection.getTitle());

        new SwitchScene().switchScene(event, "segmentsView", segmentsView);
    }

    @FXML
    private void removeCollectible(ActionEvent event) throws IOException {
        System.out.println("usuwanie obiektu");

        ConfirmDeletePopup confirmDeletePopup = new ConfirmDeletePopup();
        confirmDeletePopup.setRemoveStructureListener(this);
        confirmDeletePopup.setObjToDelete(collectible);
        confirmDeletePopup.setSourceEvent(event);
        // widok nadrzedny do powrotu
        CollectionView collectionView = new CollectionView();
        collectionView.setCollection(parentCollection);
        confirmDeletePopup.setParentView(collectionView);

        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie", confirmDeletePopup);
    }

    @FXML
    private void backToCollection(ActionEvent event) throws IOException, SQLException {
        System.out.println("powrot do kolekcji");

        CollectionView collectionView = new CollectionView();
        collectionView.setCollection(parentCollection);

        new SwitchScene().switchScene(event, "collectionView", collectionView);
    }

    private void fillMiniGallery() throws SQLException {
        miniGalleryComponent.setParentDirectory(
                String.format(collectibleThumbnailsPath,
                        System.getProperty("user.dir"), parentCollection.getTitle(), collectible.getTitle()));
        // konwersja listy thumbnails na imagemodels
        ArrayList<Thumbnail> thumbnails = thumbnailOperations.getAllThumbnails(collectible.getId());
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        for (Thumbnail thumbnail : thumbnails) {
            ImageModel imageModel = new ImageModel(thumbnail.getId(), thumbnail.getFileName(), thumbnail.getParentId());
            imageModels.add(imageModel);
        }
        miniGalleryComponent.setImages(imageModels);

        System.out.println(miniGalleryComponent.parentDirectory);
        System.out.println(miniGalleryComponent.images);
        miniGalleryComponent.fillComponent();
    }

    @Override
    public void onAddingPhotosFinished(List<File> selectedFiles) throws IOException, SQLException {
        System.out.println("CollectibleView.onAddingPhotoFinished: selectedFilesFromAddPhotosWindow "+selectedFiles);
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
    }

    @Override
    public void onDeleteConfirmed(ActionEvent event, Object objToDelete, Object view) {
        System.out.println("CollectibleView.onDeleteConfirmed: "+objToDelete.toString());

        // Spróbuj odświeżyć scenę główną
        try {
            new SwitchScene().switchScene(event, "collectionView", view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
