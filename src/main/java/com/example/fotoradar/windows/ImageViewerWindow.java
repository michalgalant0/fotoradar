package com.example.fotoradar.windows;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.ImageViewerComponent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

public class ImageViewerWindow implements Window {
    @FXML
    private ImageViewerComponent imageViewer;
    @Setter
    private Stage dialogStage;

    public void deletePhoto() throws IOException {
        System.out.println("usuniecie biezacego zdjecia, po usunieciu przejscie do nastepnego");
        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie");
    }
}
