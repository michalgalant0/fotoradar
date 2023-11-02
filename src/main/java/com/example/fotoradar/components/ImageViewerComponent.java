package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.SegmentOperations;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Thumbnail;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

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
                imageView.setImage(image);
                setCurrentImage(image); // ustawienie bieżącego zdjęcia
                setCurrentThumbnail(thumbnail);

                // Centrowanie obrazu wewnątrz ImageView
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(image.getWidth()); // Ustal szerokość na podstawie wczytanego obrazu
                imageView.setFitHeight(image.getHeight()); // Ustal wysokość na podstawie wczytanego obrazu

                // Ustal rozmiar segmentPane na podstawie rozmiaru wczytanego obrazu
                segmentPane.setMinWidth(image.getWidth());
                segmentPane.setMinHeight(image.getHeight());
                segmentPane.setMaxWidth(image.getWidth());
                segmentPane.setMaxHeight(image.getHeight());
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
            double[] xPoints = new double[4];
            double[] yPoints = new double[4];

            // Oblicz skalę dla segmentów
            double scaleX = imageViewWidth / currentImage.getWidth();
            double scaleY = imageViewHeight / currentImage.getHeight();

            for (int i = 0; i < 8; i += 2) {
                xPoints[i / 2] = coordinates[i] * scaleX;
                yPoints[i / 2] = coordinates[i + 1] * scaleY;
            }

            Canvas canvas = new Canvas(imageViewWidth, imageViewHeight);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokePolygon(xPoints, yPoints, 4);

            segmentPane.getChildren().add(canvas);
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
