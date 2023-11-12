package com.example.fotoradar.controllers.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

public class ConfirmDeletePopup implements Window {
    @FXML
    private Label label;
    @Setter
    private Stage dialogStage;

    @FXML
    public void confirmDelete() {
        System.out.println("potwierdz usuwanie");
        closeWindow(dialogStage);
    }
    @FXML
    public void cancel() {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }
}
