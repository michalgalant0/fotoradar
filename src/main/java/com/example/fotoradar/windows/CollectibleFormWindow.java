package com.example.fotoradar.windows;

import com.example.fotoradar.components.CollectibleFormComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.sql.SQLException;

public class CollectibleFormWindow implements Window {
    @FXML
    private CollectibleFormComponent collectibleForm;
    @FXML
    private Label windowLabel;

    @Setter
    private Stage dialogStage;
    @Setter
    private Collection parentCollection;

    public void initialize() {
        setWindowLabel(parentCollection.getTitle());
    }

    public void setWindowLabel(String collectionName) {
        windowLabel.setText(String.format("Dodaj obiekt do kolekcji %s", collectionName));
    }

    @FXML
    public void saveCollectible(ActionEvent event) throws SQLException {
        System.out.println("zapisz obiekt");

        System.out.println("CollectibleFormWindow.saveCollectible: parentCollection: "+parentCollection);

        Collectible collectibleToAdd = new Collectible(
                collectibleForm.titleTextField.getText(),
                collectibleForm.startDatePicker.getValue().toString(),
                collectibleForm.finishDatePicker.getValue().toString(),
                collectibleForm.descriptionTextArea.getText(),
                parentCollection.getId()
        );

        System.out.println("dane z formularza: " + collectibleToAdd);

        // dodanie obiektu do bazy
        CollectibleOperations collectibleOperations = new CollectibleOperations();
        collectibleOperations.addCollectible(collectibleToAdd);

        // utworzenie katalogu obiektu i podkatalogów MINIATURY i SEGMENTY
        String currentDirectory = System.getProperty("user.dir")+"/kolekcje/"+parentCollection.getTitle()+"/obiekty";
        File collectibleDirectory = new File(currentDirectory, collectibleToAdd.getTitle());
        File thumbnailsDir = new File(collectibleDirectory, "miniatury");
        File segmentsDir = new File(collectibleDirectory, "segmenty");

        if (!collectibleDirectory.exists()) {
            boolean directoryCreated = collectibleDirectory.mkdir();
            System.out.println(
                    directoryCreated ? "utworzono katalog obiektu" : "nie utworzono katalogu obiektu"
            );
        } else
            System.out.println("katalog obiektu istnieje");

        if (!thumbnailsDir.exists()) {
            boolean directoryCreated = thumbnailsDir.mkdir();
            System.out.println(
                    directoryCreated ? "utworzono podkatalog miniatur" : "nie utworzono podkatalogu miniatur"
            );
        } else
            System.out.println("podkatalog miniatur istnieje");

        if (!segmentsDir.exists()) {
            boolean directoryCreated = segmentsDir.mkdir();
            System.out.println(
                    directoryCreated ? "utworzono podkatalog segmentow" : "nie utworzono podkatalogu segmentow"
            );
        } else
            System.out.println("podkatalog segmentow istnieje");

        // po wykonanej operacji zamknij okienko
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }
}
