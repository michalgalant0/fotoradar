package com.example.fotoradar.windows;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
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

        String title = collectionForm.titleTextField.getText();
        String startDate = collectionForm.startDatePicker.getValue() != null ?
                collectionForm.startDatePicker.getValue().toString() : null;
        String finishDate = collectionForm.finishDatePicker.getValue() != null ?
                collectionForm.finishDatePicker.getValue().toString() : null;
        String description = collectionForm.descriptionTextArea.getText();


        // Przykład sprawdzenia i wyświetlenia komunikatu w miejscu pola tytułu
        if (title.isEmpty()) {
            // Pobranie wcześniej utworzonego pola tekstowego do wprowadzania tytułu
            TextField titleTextField = collectionForm.titleTextField;

            // Ustawienie czerwonej ramki lub tła dla pola tytułu jako wskazanie błędu
            titleTextField.setStyle("-fx-border-color: red;"); // Możesz dostosować to według potrzeb

            // Wstawienie komunikatu w miejscu pola tytułu
            titleTextField.setPromptText("Pole tytułu nie może być puste!");
            return;
        }



        Collection collectionToAdd = new Collection(title, startDate, finishDate, description);

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
