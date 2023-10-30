package com.example.fotoradar.windows;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
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

        // dodanie kolekcji do bazy
        CollectionOperations collectionOperations = new CollectionOperations();
        collectionOperations.addCollection(collectionToAdd);

        // utworzenie katalogu kolekcji i podkatalogu obiekty
        new DirectoryOperator().createStructure(collectionToAdd);

        // zamkniecie okienka po wykonanej operacji
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }
}
