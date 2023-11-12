package com.example.fotoradar.controllers.windows;

import com.example.fotoradar.controllers.components.ImageViewerComponent;
import com.example.fotoradar.models.ImageModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;

public class ImageViewerWindow implements Window {
    @FXML
    private Label windowLabel;
    @FXML
    private ImageViewerComponent imageViewer;
    @Setter
    private Stage dialogStage;

    @Setter
    private String parentDirectory;
    @Setter
    private Image currentImage;
    @Setter
    private int currentImageIndex;
    @Setter
    private ArrayList<ImageModel> images;

    public void initialize() throws SQLException {
        windowLabel.setText(parentDirectory+'\n'+currentImage.toString()+'\n'+currentImage.getUrl());
        imageViewer.setParentDirectory(parentDirectory);
        imageViewer.setImages(images);
        imageViewer.setCurrentImage(currentImage);
        imageViewer.setCurrentImageIndex(currentImageIndex);
        imageViewer.initialize();
    }
}