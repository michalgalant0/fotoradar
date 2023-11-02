package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.SegmentOperations;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Thumbnail;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.Getter;
import lombok.Setter;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImageViewerComponent extends AnchorPane {
    @FXML
    private ImageView imageView;
    @FXML
    private Pane segmentPane;

    @Setter
    private String parentDirectory;
    @Setter
    private ArrayList<Thumbnail> thumbnails = new ArrayList<>();
    @Setter @Getter
    private Image currentImage;
    @Setter @Getter
    private Thumbnail currentThumbnail;
    private int currentImageIndex;
    @Setter
    private boolean isForSegmentsView = false;

    @Getter
    private Polygon highlightedPolygon; // Zmienna do przechowywania zaznaczonego poligonu

    public ImageViewerComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/ImageViewerComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() throws SQLException {
        currentImageIndex = 0;
        showImage();
    }

    @FXML
    private void showPreviousImage() throws SQLException {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            showImage();
        } else if (currentImageIndex == 0) {
            currentImageIndex = thumbnails.size() - 1;
            showImage();
        }
    }

    @FXML
    private void showNextImage() throws SQLException {
        if (currentImageIndex < thumbnails.size() - 1) {
            currentImageIndex++;
            showImage();
        } else if (currentImageIndex == thumbnails.size() - 1) {
            currentImageIndex = 0;
            showImage();
        }
    }

    @FXML
    private void deleteCurrentPhoto() throws IOException {
        System.out.println("usuwanie zdjęcia");
        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie");
    }

    private void showImage() throws SQLException {
        if (!thumbnails.isEmpty() && currentImageIndex >= 0 && currentImageIndex < thumbnails.size()) {
            Thumbnail thumbnail = thumbnails.get(currentImageIndex);
            String imagePath = parentDirectory + thumbnail.getFileName();
            try {
                FileInputStream fileInputStream = new FileInputStream(imagePath);
                Image image = new Image(fileInputStream);

                // Ustal szerokość i wysokość obrazka, aby zmieścił się w kontenerze (max 800x800)
                double maxWidth = 800;
                double maxHeight = 800;
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();

                if (imageWidth > maxWidth || imageHeight > maxHeight) {
                    double widthRatio = maxWidth / imageWidth;
                    double heightRatio = maxHeight / imageHeight;
                    double scaleFactor = Math.min(widthRatio, heightRatio);

                    imageWidth *= scaleFactor;
                    imageHeight *= scaleFactor;
                }

                imageView.setImage(image);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(imageWidth);
                imageView.setFitHeight(imageHeight);

                setCurrentImage(image); // ustawienie bieżącego zdjęcia
                setCurrentThumbnail(thumbnail);

                // Ustal rozmiar segmentPane na podstawie rozmiaru wczytanego obrazu
                segmentPane.setMinWidth(imageWidth);
                segmentPane.setMinHeight(imageHeight);
                segmentPane.setMaxWidth(imageWidth);
                segmentPane.setMaxHeight(imageHeight);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (isForSegmentsView) {
            loadSegments();
        }
    }

    private void loadSegments() throws SQLException {
        System.out.println("wyświetl segmenty na danym zdjęciu");
        ArrayList<Segment> segments = new SegmentOperations().getAllSegmentsForThumbnail(currentThumbnail.getId());

        segmentPane.getChildren().clear(); // Wyczyść istniejące segmenty

        double imageViewWidth = imageView.getFitWidth();
        double imageViewHeight = imageView.getFitHeight();

        for (Segment segment : segments) {
            drawSegment(segment, imageViewWidth, imageViewHeight);
        }
    }

    private void drawSegment(Segment segment, double imageViewWidth, double imageViewHeight) {
        String coords = segment.getCoords();
        double[] coordinates = parseCoordinates(coords);

        if (coordinates.length == 8) {
            double scaleX = imageViewWidth / currentImage.getWidth();
            double scaleY = imageViewHeight / currentImage.getHeight();

            Polygon polygon = new Polygon();

            for (int i = 0; i < 8; i += 2) {
                double x = coordinates[i] * scaleX;
                double y = coordinates[i + 1] * scaleY;
                polygon.getPoints().addAll(x, y);
            }

            polygon.setStroke(Color.RED);
            polygon.setStrokeWidth(2);
            polygon.setFill(Color.TRANSPARENT); // Ustaw wypełnienie na przezroczyste

            segmentPane.getChildren().add(polygon);

            // fancy gowienko - mozna usunac, ale cieszy XD
            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), polygon);
            scaleIn.setFromX(1.0);
            scaleIn.setFromY(1.0);
            scaleIn.setToX(1.1);
            scaleIn.setToY(1.1);

            polygon.setOnMouseClicked(event -> {
                System.out.println("Kliknięto segment: " + segment);

                // Odznacz poprzedni zaznaczony segment
                if (highlightedPolygon != null) {
                    // animacja
                    ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), highlightedPolygon);
                    scaleOut.setFromX(1.1);
                    scaleOut.setFromY(1.1);
                    scaleOut.setToX(1.0);
                    scaleOut.setToY(1.0);
                    scaleOut.play();
                    // koniec animacji
                    highlightedPolygon.setStroke(Color.RED);
                    highlightedPolygon.setFill(Color.TRANSPARENT);
                }

                // Zaznacz bieżący segment
                scaleIn.play(); // animacja
                polygon.setStroke(Color.PURPLE);
                polygon.setFill(Color.rgb(127,50,120,0.2));
                highlightedPolygon = polygon;
            });
        }
    }


    private static double[] parseCoordinates(String coordinates) {
        double[] result = new double[8];
        // Usuń zbędne spacje
        coordinates = coordinates.replaceAll("\\s+", "");
        System.out.println(coordinates);
        // Usuń nawiasy okrągłe
        coordinates = coordinates
                .replace(")(",",")
                .replace("(", "")
                .replace(")", "");
        // Podziel ciąg znaków na współrzędne
        String[] parts = coordinates.split(",");
        // Sprawdź, czy mamy wystarczającą liczbę części
        if (parts.length != 8)
            throw new IllegalArgumentException("Nieprawidłowy format współrzędnych. Oczekiwano 8 liczb.");
        // Konwertuj ciągi znaków na liczby double
        for (int i = 0; i < 8; i++)
            result[i] = Double.parseDouble(parts[i]);
        return result;
    }
}
