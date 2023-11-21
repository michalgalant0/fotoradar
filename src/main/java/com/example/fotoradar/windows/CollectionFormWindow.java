package com.example.fotoradar.windows;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;

public class CollectionFormWindow implements Window {
    @FXML
    private CollectionFormComponent collectionForm;
    @Setter
    private Stage dialogStage;

    @Setter
    private OnWindowClosedListener onWindowClosedListener;

    @FXML
    public void saveCollection(ActionEvent event) throws SQLException {
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
        DirectoryOperator.getInstance().createStructure(collectionToAdd);

        // zamkniecie okienka po wykonanej operacji
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }
}
