package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.ImageModel;
import com.example.fotoradar.windows.ImageViewerWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

public class MiniGalleryComponent extends AnchorPane {
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public GridPane photosContainer;
    @Setter
    public String parentDirectory;
    @Setter
    public ArrayList<ImageModel> images = new ArrayList<>();

    public MiniGalleryComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/MiniGalleryComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
    }

    public void fillComponent() {
        System.out.println(parentDirectory);
        System.out.println(images);

        int maxColumns = 3; // Ilość kolumn
        int columnIndex = 0;
        int rowIndex = 0;

        for (int i=0; i< images.size(); i++) {
            ImageModel imageModel = images.get(i);
            System.out.println("MiniGallery.fillComponent: thumbnail name: "+imageModel.getFileName());
            ImageView imageView = createThumbnailImageView(imageModel.getFileName(), i);
            photosContainer.add(imageView, columnIndex, rowIndex);

            // Przesuwaj się do kolejnej kolumny lub wiersza
            columnIndex++;
            if (columnIndex >= maxColumns) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    private ImageView createThumbnailImageView(String thumbnailName, int index) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        String filePath = parentDirectory + thumbnailName;
        System.out.println("miniGallery.createThumbnailImageView: filePath "+filePath);
        Image image = new Image("file://"+filePath);
        imageView.setImage(image);

        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Kliknięto lewym przyciskiem myszy na miniaturze obrazu: " + thumbnailName);
                ImageViewerWindow imageViewerWindow = new ImageViewerWindow();
                imageViewerWindow.setParentDirectory(parentDirectory);
                imageViewerWindow.setImages(images);
                imageViewerWindow.setCurrentImage(image);
                imageViewerWindow.setCurrentImageIndex(index);
                try {
                    new SwitchScene().displayWindow("ImageViewerWindow", "przegląd zdjęć", imageViewerWindow);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                System.out.println("Kliknięto prawym przyciskiem myszy na miniaturze obrazu: " + thumbnailName);
            }
        });

        return imageView;
    }
}
