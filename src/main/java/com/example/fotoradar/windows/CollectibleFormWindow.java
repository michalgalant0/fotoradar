package com.example.fotoradar.windows;

import com.example.fotoradar.DirectoryOperator;
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

    private void setWindowLabel(String collectionName) {
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
        new DirectoryOperator().createStructure(collectibleToAdd, parentCollection.getTitle());

        // po wykonanej operacji zamknij okienko
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }
}
