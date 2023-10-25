package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImageViewerComponent extends AnchorPane {

    @FXML
    private ImageView imageView;

    private ArrayList<String> imageFiles = new ArrayList<>();
    private int currentImageIndex = 0;

    public ImageViewerComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/ImageViewerComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
        try {
            String directoryName = "tmpPhotos";
            Path directoryPath = Paths.get(
                    Objects.requireNonNull(Main.class.getClassLoader().getResource(directoryName)).toURI()
            );

            imageFiles = Files.walk(directoryPath, 1)
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toCollection(ArrayList::new));

            showImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPreviousImage() {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            showImage();
        } else if (currentImageIndex == 0) {
            currentImageIndex = imageFiles.size() - 1;
            showImage();
        }
    }

    public void showNextImage() {
        if (currentImageIndex < imageFiles.size() - 1) {
            currentImageIndex++;
            showImage();
        } else if (currentImageIndex == imageFiles.size() - 1) {
            currentImageIndex = 0;
            showImage();
        }
    }

    private void showImage() {
        if (!imageFiles.isEmpty() && currentImageIndex >= 0 && currentImageIndex < imageFiles.size()) {
            String imagePath = imageFiles.get(currentImageIndex);
            try {
                FileInputStream fileInputStream = new FileInputStream(imagePath);
                Image image = new Image(fileInputStream);
                imageView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
