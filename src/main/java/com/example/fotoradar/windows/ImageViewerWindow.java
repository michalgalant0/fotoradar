package com.example.fotoradar.windows;

import com.example.fotoradar.components.ImageViewerComponent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

public class ImageViewerWindow implements Window {
    @FXML
    private ImageViewerComponent imageViewer;
    @Setter
    private Stage dialogStage;

    public void deletePhoto() {
        System.out.println("usuniecie biezacego zdjecia");
    }
}
