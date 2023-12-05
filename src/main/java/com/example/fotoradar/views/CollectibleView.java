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
import com.example.fotoradar.windows.AddPhotosWindow;
import com.example.fotoradar.windows.ConfirmDeletePopup;
import com.example.fotoradar.windows.OnWindowClosedListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CollectibleView implements AddPhotoListener, RemoveStructureListener, OnWindowClosedListener {
    @FXML
    private Label windowLabel;
    @FXML
    public MiniGalleryComponent miniGalleryComponent;
    @FXML
    public CollectibleFormComponent collectibleFormComponent;

    @Setter
    private Collectible collectible;
    @Setter
    private Collection parentCollection;

    private String collectibleThumbnailsPathTmp = "%s/KOLEKCJE/%s/OBIEKTY/%s/MINIATURY/";
    private String currentCollectibleThumnailsPath;
    private ThumbnailOperations thumbnailOperations;
    private CollectibleOperations collectibleOperations;

    public CollectibleView() {
        try {
            thumbnailOperations = new ThumbnailOperations();
            collectibleOperations = new CollectibleOperations();
            collectible = new Collectible();
            parentCollection = new Collection();
            currentCollectibleThumnailsPath = "";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() throws SQLException {
        System.out.println("CollectibleView.initialize: "+collectible);
        // ustawienie kolekcji nadrzędnej
        setParentCollection();
        // ustawienie katalogu miniatur dla bieżącego obiektu
        currentCollectibleThumnailsPath = String.format(collectibleThumbnailsPathTmp,
                System.getProperty("user.dir"), parentCollection.getTitle(), collectible.getTitle());
        // ustawienie nagłówka okna
        setWindowLabel(parentCollection.getTitle(), collectible.getTitle());
        // wypełnienie komponentu z formularzem
        fillCollectibleForm();
        // wypelnienie komponentu miniGallery miniaturami
        fillMiniGallery();

        DirectoryOperator.getInstance().createStructure(collectible, parentCollection.getTitle());
    }

    private void setParentCollection() throws SQLException {
        parentCollection = new CollectionOperations().getCollectionById(collectible.getParentCollectionId());
    }

    private void setWindowLabel(String collectionName, String collectibleName) {
        windowLabel.setText(String.format("kolekcje/ %s/ %s", collectionName, collectibleName));
    }

    private void fillCollectibleForm() {
        collectibleFormComponent.setTitleTextField(collectible.getTitle());
        collectibleFormComponent.setStartDatePicker(collectible.getStartDate());
        collectibleFormComponent.setFinishDatePicker(collectible.getFinishDate());
        collectibleFormComponent.setDescriptionTextArea(collectible.getDescription());
        collectibleFormComponent.setStatusComboBox(collectible.getStatus());
    }

    @FXML
    private void saveCollectible(ActionEvent event) throws SQLException {
        String oldPath = String.format("%s/KOLEKCJE/%s/OBIEKTY/%s",
                System.getProperty("user.dir"), parentCollection.getTitle(), collectible.getTitle());

        collectible.setTitle(collectibleFormComponent.titleTextField.getText());
        collectible.setStartDate(collectibleFormComponent.startDatePicker.getValue().toString());
        collectible.setFinishDate(collectibleFormComponent.finishDatePicker.getValue().toString());
        collectible.setDescription(collectibleFormComponent.descriptionTextArea.getText());

        // update obiektu do bazy
        collectibleOperations.updateCollectible(collectible);

        // aktualizacja nazwy katalogu
        String newName = collectible.getTitle();
        System.err.println(oldPath);
        System.err.println(newName);
        DirectoryOperator.getInstance().updateDirectoryName(oldPath, newName);

        System.out.println("zapis obiektu");
        // pozostaje na tym samym widoku
        refresh();
    }

    @FXML
    private void addSketch(ActionEvent event) {
        System.out.println("dodawanie szkicu");
        // otwarcie modułu do szkicowania
    }

    @FXML
    private void addPhoto(ActionEvent event) throws IOException {
        System.out.println("dodawanie zdjęcia");

        AddPhotosWindow addPhotosWindow = new AddPhotosWindow();
        addPhotosWindow.setAddPhotoListener(this);
        addPhotosWindow.setOnWindowClosedListener(this);

        new SwitchScene().displayWindow("AddPhotosWindow", "Dodaj miniatury", addPhotosWindow);
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        miniGalleryComponent.setOnWindowClosedListener(this);
        miniGalleryComponent.setParentDirectory(
                String.format(currentCollectibleThumnailsPath,
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
            String destinationFilePath = currentCollectibleThumnailsPath + file.getName();

            // Zapisz przeskalowany obraz do pliku
            ImageIO.write(scaledBufferedImage, "jpg", new File(destinationFilePath));

            // Dodaj miniaturę do bazy
            thumbnailOperations.addThumbnail(new Thumbnail(file.getName(), collectible.getId()));
        }

        // Odśwież widok
        refresh();
    }

    @Override
    public void onDeleteConfirmed(ActionEvent event, Object view) {
        System.out.println("CollectibleView.onDeleteConfirmed: "+collectible);

        // usuwanie z bazy
        try {
            if (new CollectibleOperations().deleteCollectibleWithSubstructures(collectible.getId()))
                System.out.println("usunieto obiekt z bazy");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // usuwanie katalogow
        DirectoryOperator.getInstance().removeStructure(collectible, parentCollection.getTitle());

        // Spróbuj odświeżyć scenę główną
        try {
            new SwitchScene().switchScene(event, "collectionView", view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refresh() throws SQLException {
        setCollectible(collectibleOperations.getCollectibleById(collectible.getId()));
        // ustawienie nagłówka okna
        setWindowLabel(parentCollection.getTitle(), collectible.getTitle());
        // ustawienie katalogu miniatur dla bieżącego obiektu
        currentCollectibleThumnailsPath = String.format(collectibleThumbnailsPathTmp,
                System.getProperty("user.dir"), parentCollection.getTitle(), collectible.getTitle());

        // wypełnienie komponentu z formularzem
        fillCollectibleForm();
        // wypelnienie komponentu miniGallery miniaturami
        fillMiniGallery();
    }

    @Override
    public void onWindowClosed() {
        System.err.println("REFRESH NA COLLECTIBLE VIEW");
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
