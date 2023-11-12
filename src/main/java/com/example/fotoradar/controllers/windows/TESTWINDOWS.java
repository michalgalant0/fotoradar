package com.example.fotoradar.controllers.windows;

import com.example.fotoradar.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TESTWINDOWS {
    public void formularzkolekcji(ActionEvent event) throws IOException {
        displayWindow("CollectionFormWindow", "formularz kolekcji");
    }
    public void formularzobiektu(ActionEvent event) throws IOException {
        displayWindow("CollectibleFormWindow", "formularz obiektu");
    }
    public void formularzwersji(ActionEvent event) throws IOException {
        displayWindow("VersionFormWindow", "formularz wersji");
    }
    public void dodajzdjecia(ActionEvent event) throws IOException {
        displayWindow("AddPhotosWindow", "dodaj zdjecia");
    }
    public void przeglad(ActionEvent event) throws IOException {
        displayWindow("ImageViewerWindow", "przeglad zdjec");
    }
    public void potwierdzusuwanie(ActionEvent event) throws IOException {
        displayWindow("ConfirmDeletePopup", "potwierdz usuwanie");
    }

    public void displayWindow(String windowName, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("windows/"+windowName+".fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle(title);

        // Ustawienie sceny na nowym oknie
        dialogStage.setScene(scene);

        // Pobranie kontrolera formularza kolekcji
        Window controller = fxmlLoader.getController();
        controller.setDialogStage(dialogStage);

        // Wyświetlenie nowego okna
        dialogStage.showAndWait();
    }
}
