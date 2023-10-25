package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;

public class MiniGalleryComponent extends AnchorPane {
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public GridPane photosContainer;
    private ArrayList<String> imagePaths = new ArrayList<>();

    public MiniGalleryComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/MiniGalleryComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
        int maxColumns = 3; // Ilość kolumn
        int columnIndex = 0;
        int rowIndex = 0;

        for (String imagePath : imagePaths) {
            ImageView thumbnail = createThumbnailImageView(imagePath);
            photosContainer.add(thumbnail, columnIndex, rowIndex);

            // Przesuwaj się do kolejnej kolumny lub wiersza
            columnIndex++;
            if (columnIndex >= maxColumns) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    private ImageView createThumbnailImageView(String imagePath) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        Image image = new Image("file:" + imagePath); // Załóżmy, że obrazy znajdują się lokalnie na dysku
        imageView.setImage(image);

        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Kliknięto lewym przyciskiem myszy na miniaturze obrazu: " + imagePath);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                System.out.println("Kliknięto prawym przyciskiem myszy na miniaturze obrazu: " + imagePath);
            }
        });

        return imageView;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
