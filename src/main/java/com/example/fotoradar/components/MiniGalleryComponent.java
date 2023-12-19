package com.example.fotoradar.components;

import com.example.fotoradar.ImageViewerFlag;
import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.ImageModel;
import com.example.fotoradar.views.CollectibleView;
import com.example.fotoradar.views.VersionView;
import com.example.fotoradar.windows.ImageViewerWindow;
import com.example.fotoradar.windows.OnWindowClosedListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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

    @Setter
    private OnWindowClosedListener onWindowClosedListener;

    public MiniGalleryComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/MiniGalleryComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
    }

    // todo poprawic bo sie layout zdjec popsul
    public void fillComponent() {
        photosContainer.getChildren().clear();

        if (images == null || images.isEmpty()) {
            Label infoLabel = new Label("NIE MA ŻADNEGO ZDJĘCIA");
            infoLabel.setStyle("""
                    -fx-font-size: 20;
                    -fx-text-fill: grey;
                    -fx-alignment: center;
                    """);
            photosContainer.setAlignment(Pos.CENTER);
            photosContainer.getChildren().add(infoLabel);
        }
        else {
            photosContainer.setAlignment(Pos.TOP_LEFT);
            System.out.println(parentDirectory);
            System.out.println(images);

            final int MAX_COLUMNS = 3;
            int columnIndex = 0;
            int rowIndex = 0;

            for (int i = 0; i < images.size(); i++) {
                ImageModel imageModel = images.get(i);
                System.out.println("MiniGallery.fillComponent: thumbnail name: " + imageModel.getFileName());
                ImageView imageView = createThumbnailImageView(imageModel.getFileName(), i);
                photosContainer.add(imageView, columnIndex, rowIndex);

                // Przesuwaj się do kolejnej kolumny lub wiersza
                columnIndex++;
                if (columnIndex >= MAX_COLUMNS) {
                    columnIndex = 0;
                    rowIndex++;
                }
            }
        }
    }

    private ImageView createThumbnailImageView(String thumbnailName, int index) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true); // Preserve the aspect ratio of the image

        String filePath = Paths.get(parentDirectory, thumbnailName).toString();
        System.out.println("miniGallery.createThumbnailImageView: filePath " + filePath);
        Image image = new Image("file:" + filePath);
        imageView.setImage(image);

        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Kliknięto lewym przyciskiem myszy na miniaturze obrazu: " + thumbnailName);
                ImageViewerWindow imageViewerWindow = getImageViewerWindow(index, image);

                try {
                    SwitchScene.getInstance().displayWindow("ImageViewerWindow", "przegląd zdjęć", imageViewerWindow, onWindowClosedListener);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                System.out.println("Kliknięto prawym przyciskiem myszy na miniaturze obrazu: " + thumbnailName);
            }
        });

        // Set alignment of the ImageView within the GridPane cell
        GridPane.setHalignment(imageView, HPos.CENTER);
        GridPane.setValignment(imageView, VPos.CENTER);

        return imageView;
    }

    private ImageViewerWindow getImageViewerWindow(int index, Image image) {
        ImageViewerWindow imageViewerWindow = new ImageViewerWindow();
        imageViewerWindow.setParentDirectory(removeTmpFromPath(parentDirectory));
        imageViewerWindow.setImages(images);
        imageViewerWindow.setCurrentImage(image);
        imageViewerWindow.setCurrentImageIndex(index);

        // ustawienie odpowiedniej flagi dla ImageViewerComponent
        if (onWindowClosedListener instanceof CollectibleView)
            imageViewerWindow.setImageViewerFlag(ImageViewerFlag.COLLECTIBLE);
        else if (onWindowClosedListener instanceof VersionView)
            imageViewerWindow.setImageViewerFlag(ImageViewerFlag.VERSION);
        return imageViewerWindow;
    }

    private String removeTmpFromPath(String path) {
        if (path.contains(".tmp")) {
            // Sprawdź, czy ścieżka zawiera ".tmp"
            int tmpIndex = path.indexOf(".tmp");
            if (tmpIndex != -1) {
                // Usuń ".tmp" i separator ścieżki po nim
                path = path.substring(0, tmpIndex) + path.substring(tmpIndex + 4);
                if (path.charAt(tmpIndex - 1) == File.separator.charAt(0)) {
                    path = path.substring(0, tmpIndex - 1) + path.substring(tmpIndex);
                }
            }
        }
        return path;
    }
}
