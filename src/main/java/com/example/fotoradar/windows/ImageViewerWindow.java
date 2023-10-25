package com.example.fotoradar.windows;

import com.example.fotoradar.components.ImageViewer;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

public class ImageViewerWindow implements Window {
    @FXML
    private ImageViewer imageViewer;
    @Setter
    private Stage dialogStage;

    public void deletePhoto() {
        System.out.println("usuniecie biezacego zdjecia");
    }
}
