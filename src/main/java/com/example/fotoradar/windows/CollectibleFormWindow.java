package com.example.fotoradar.windows;

import com.example.fotoradar.components.CollectibleFormComponent;
import com.example.fotoradar.models.Collectible;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

public class CollectibleFormWindow implements Window {
    @FXML
    private CollectibleFormComponent collectibleForm;
    @Setter
    private Stage dialogStage;

    @FXML
    public void saveCollectible(ActionEvent event) {
        System.out.println("zapisz obiekt");

        Collectible collectionToAdd = new Collectible(
                collectibleForm.titleTextField.getText(),
                collectibleForm.startDatePicker.getValue().toString(),
                collectibleForm.finishDatePicker.getValue().toString(),
                collectibleForm.descriptionTextArea.getText(),
                // todo dodac przekazywanie id kolekcji
                8
        );

        System.out.println("dane z formularza: " + collectionToAdd);

        // po wykonanej operacji zamknij okienko
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }
}
