package com.example.fotoradar.windows;

import javafx.stage.Stage;

public interface Window {
    void setDialogStage (Stage dialogStage);
    default void closeWindow(Stage dialogStage) {
        dialogStage.close();
    }
}
