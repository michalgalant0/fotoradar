package com.example.fotoradar.windows;

import com.example.fotoradar.RemoveStructureListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

public class ConfirmDeletePopup implements Window {
    @FXML
    private Label label;
    @Setter
    private Stage dialogStage;

    @Setter
    private RemoveStructureListener removeStructureListener;

    @Setter
    private Object parentView;
    @Setter
    private ActionEvent sourceEvent;

    @FXML
    public void confirmDelete(ActionEvent event) {
        System.out.println("potwierdz usuwanie");

        removeStructureListener.onDeleteConfirmed(sourceEvent, parentView);

        closeWindow(dialogStage);
    }
    @FXML
    public void cancel() {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }
}
