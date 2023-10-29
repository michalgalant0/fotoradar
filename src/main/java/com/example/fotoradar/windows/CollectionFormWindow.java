package com.example.fotoradar.windows;

import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.views.CollectionView;
import com.example.fotoradar.views.CollectionsView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CollectionFormWindow implements Window {
    @FXML
    private CollectionFormComponent collectionForm;
    @Setter
    private Stage dialogStage;

    @FXML
    public void saveCollection(ActionEvent event) throws SQLException, IOException {
        System.out.println("zapisz kolekcje");

        Collection collectionToAdd = new Collection(
                collectionForm.titleTextField.getText(),
                collectionForm.startDatePicker.getValue().toString(),
                collectionForm.finishDatePicker.getValue().toString(),
                collectionForm.descriptionTextArea.getText()
        );

        System.out.println("dane z formularza: " + collectionToAdd);

        CollectionOperations collectionOperations = new CollectionOperations();
        collectionOperations.addCollection(collectionToAdd);

        // zamkniecie okienka po wykonanej operacji
        closeWindow(dialogStage);

        // utworzenie katalogu kolekcji i podkatalogu obiekty
        String currentDirectory = System.getProperty("user.dir")+"/kolekcje";
        File collectionDirectory = new File(currentDirectory, collectionToAdd.getTitle());
        File collectiblesDirectory = new File(collectionDirectory, "obiekty");

        if (!collectionDirectory.exists()) {
            boolean directoryCreated = collectionDirectory.mkdir();
            System.out.println(
                    directoryCreated ? "utworzono katalog kolekcji" : "nie utworzono katalogu kolekcji"
            );
        } else
            System.out.println("katalog kolekcji istnieje");

        if (!collectiblesDirectory.exists()) {
            boolean directoryCreated = collectiblesDirectory.mkdir();
            System.out.println(
                    directoryCreated ? "utworzono podkatalog obiektow" : "nie utworzono podkatalogu obiektow"
            );
        } else
            System.out.println("podkatalog obiektow istnieje");
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }
}
