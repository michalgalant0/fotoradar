package com.example.fotoradar.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.example.fotoradar.windows.Window;
import lombok.Setter;



public class SettingsWindow implements Window{

    @FXML
    private Button setCancel;
    @FXML
    private Button setSaveSettings;

    @Setter
    private Stage dialogStage;

    @FXML
    private void setCancel(){
        cancel();
    }
    @FXML
    private void setSaveSettings(){
        saveSettings();
    }

    private void saveSettings() {
        System.out.println("zapis ustawień");
    }

    private void cancel() {
        closeWindow(dialogStage);
    }
}
