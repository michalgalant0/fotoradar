package com.example.fotoradar.windows;

import com.example.fotoradar.components.CollectionForm;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

public class CollectionFormWindow implements Window {
    @FXML
    private CollectionForm collectionForm;
    @Setter
    private Stage dialogStage;

    @FXML
    public void saveCollection(ActionEvent event) {
        System.out.println("zapisz kolekcje");

        Collection collectionToAdd = new Collection(
                collectionForm.titleTextField.getText(),
                collectionForm.startDatePicker.getValue().toString(),
                collectionForm.finishDatePicker.getValue().toString(),
                collectionForm.descriptionTextArea.getText()
        );

        System.out.println("dane z formularza: " + collectionToAdd);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
    }
}
