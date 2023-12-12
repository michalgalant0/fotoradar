package com.example.fotoradar.views;

import com.example.fotoradar.*;
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
import com.example.fotoradar.windows.OnWindowClosedListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
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
    private Painter painter;

    private String collectibleThumbnailsPathTmp = Paths.get("%s","KOLEKCJE","%s","OBIEKTY","%s","MINIATURY").toString();
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
                Main.getDefPath(), parentCollection.getTitle(), collectible.getTitle());
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

        String startDate = collectible.getStartDate();
        collectibleFormComponent.setStartDatePicker(startDate != null ? startDate : null);

        String finishDate = collectible.getFinishDate();
        collectibleFormComponent.setFinishDatePicker(finishDate != null ? finishDate : null);

        String description = collectible.getDescription();
        collectibleFormComponent.setDescriptionTextArea(description != null ? description : null);

        collectibleFormComponent.setStatusComboBox(collectible.getStatus());
    }


    @FXML
    private void saveCollectible(ActionEvent event) throws SQLException {
        String oldPath = String.format(Paths.get("%s","KOLEKCJE","%s","OBIEKTY","%s").toString(),
                Main.getDefPath(), parentCollection.getTitle(), collectible.getTitle());

        String title = collectibleFormComponent.titleTextField.getText();
        // Przykład sprawdzenia i wyświetlenia komunikatu w miejscu pola tytułu
        if (title.isEmpty()) {
            // Pobranie wcześniej utworzonego pola tekstowego do wprowadzania tytułu
            TextField titleTextField = collectibleFormComponent.titleTextField;

            // Ustawienie czerwonej ramki lub tła dla pola tytułu jako wskazanie błędu
            titleTextField.setStyle("-fx-border-color: red;"); // Możesz dostosować to według potrzeb

            // Wstawienie komunikatu w miejscu pola tytułu
            titleTextField.setPromptText("Pole tytułu nie może być puste!");
            return;
        }
        else{
            // Jeśli tytuł nie jest pusty, przywróć domyślny styl pola tekstowego
            TextField titleTextField = collectibleFormComponent.titleTextField;
            titleTextField.setStyle(""); // Usunięcie dodanego stylu (reset do domyślnego)

            // Możesz także usunąć komunikat, jeśli taki został wyświetlony
            titleTextField.setPromptText(""); // Usunięcie wyświetlonego komunikatu
        }
        collectible.setTitle(title);

        LocalDate startDateValue = collectibleFormComponent.startDatePicker.getValue();
        collectible.setStartDate(startDateValue != null ? startDateValue.toString() : null);

        LocalDate finishDateValue = collectibleFormComponent.finishDatePicker.getValue();
        collectible.setFinishDate(finishDateValue != null ? finishDateValue.toString() : null);

        collectible.setDescription(collectibleFormComponent.descriptionTextArea.getText());
        collectible.setStatus(collectibleFormComponent.statusComboBox.getValue());

        // Update obiektu do bazy
        collectibleOperations.updateCollectible(collectible);

        // Aktualizacja nazwy katalogu
        String newName = collectible.getTitle();
        System.err.println(oldPath);
        System.err.println(newName);
        DirectoryOperator.getInstance().updateDirectoryName(oldPath, newName);

        System.out.println("Zapis obiektu");
        // Pozostaje na tym samym widoku
        refresh();
    }


    @FXML
    private void addSketch(ActionEvent event) throws Exception {
        System.out.println("dodawanie szkicu");
        // otwarcie modułu do szkicowania
        Painter painter = new Painter();
        painter.setParentCollectionName(parentCollection.getTitle());
        painter.setCollectible(collectible);
        painter.setOnWindowClosedListener(this);
        Stage stage = new Stage();
        painter.start(stage);
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
                        Main.getDefPath(), parentCollection.getTitle(), collectible.getTitle()));
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
            String destinationFilePath = Paths.get(currentCollectibleThumnailsPath, file.getName()).toString();

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
                Main.getDefPath(), parentCollection.getTitle(), collectible.getTitle());

        // wypełnienie komponentu z formularzem
        //fillCollectibleForm();
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
