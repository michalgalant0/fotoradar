package com.example.fotoradar.views;
import javafx.application.Platform;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
public class WindowManager {

    public static void setFullScreen(Stage stage) {
        if (stage != null) {
            stage.setFullScreen(true);
        }

    }


    // Możesz dodać tutaj więcej metod zarządzających oknami, jeśli potrzebujesz.
}