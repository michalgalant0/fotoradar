package com.example.fotoradar.windows;

import com.example.fotoradar.components.VersionFormComponent;
import com.example.fotoradar.models.Version;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

public class VersionFormWindow implements Window {
    @FXML
    public VersionFormComponent versionForm;
    @Setter
    private Stage dialogStage;

    @FXML
    public void saveVersion(ActionEvent event) {
        System.out.println("zapisz wersję");

        Version versionToAdd = new Version(
                versionForm.nameTextField.getText(),
                versionForm.startDatePicker.getValue().toString(),
                versionForm.finishDatePicker.getValue().toString(),
                // todo dodac przekazywanie id zespolu
                1,
                // todo dodac przekazywanie id segmentu
                8
        );

        System.out.println("dane z formularza: " + versionToAdd);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
    }
}
