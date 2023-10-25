package com.example.fotoradar.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

public class AddPhotosWindow implements Window {
    @Setter
    private Stage dialogStage;

    @FXML
    public void addPhotos(ActionEvent event) {
        System.out.println("dodanie zdjęć");
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anulowanie");
        closeWindow(dialogStage);
    }
}
