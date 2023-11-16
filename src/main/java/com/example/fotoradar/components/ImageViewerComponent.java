package com.example.fotoradar.components;

import com.example.fotoradar.*;
import com.example.fotoradar.databaseOperations.PhotoOperations;
import com.example.fotoradar.databaseOperations.SegmentOperations;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.ImageModel;
import com.example.fotoradar.models.Photo;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Thumbnail;
import com.example.fotoradar.windows.ConfirmDeletePopup;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImageViewerComponent extends AnchorPane implements RemoveStructureListener {
    @FXML
    private ImageView imageView;
    @FXML
    private Pane segmentPane;

    @Setter
    private String parentDirectory;
    @Setter
    private ArrayList<ImageModel> images = new ArrayList<>();
    @Setter @Getter
    private Image currentImage;
    @Setter @Getter
    private Thumbnail currentThumbnail;
    @Setter @Getter
    private Photo currentPhoto;
    @Setter
    private int currentImageIndex;
    @Setter
    private boolean isForSegmentsView = false;

    @Getter
    private Polygon highlightedPolygon; // Zmienna do przechowywania zaznaczonego poligonu

    @Setter
    private SegmentsListener segmentsListener;

    public ImageViewerComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/ImageViewerComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() throws SQLException {
        showImage(currentImageIndex);
    }

    @FXML
    private void showPreviousImage() throws SQLException {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            showImage(currentImageIndex);
        } else if (currentImageIndex == 0) {
            currentImageIndex = images.size() - 1;
            showImage(currentImageIndex);
        }
    }

    @FXML
    private void showNextImage() throws SQLException {
        if (currentImageIndex < images.size() - 1) {
            currentImageIndex++;
            showImage(currentImageIndex);
        } else if (currentImageIndex == images.size() - 1) {
            currentImageIndex = 0;
            showImage(currentImageIndex);
        }
    }

    @FXML
    private void deleteCurrentPhoto(ActionEvent event) throws IOException {
        System.out.println("usuwanie zdjęcia");

        ConfirmDeletePopup confirmDeletePopup = new ConfirmDeletePopup();
        confirmDeletePopup.setRemoveStructureListener(this);
        confirmDeletePopup.setSourceEvent(event);
        confirmDeletePopup.setParentView(event.getSource());

        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie", confirmDeletePopup);
    }

    private void showImage(int index) throws SQLException {
        if (!images.isEmpty() && currentImageIndex >= 0 && currentImageIndex < images.size()) {
            currentImageIndex = index;
            ImageModel imageModel = images.get(currentImageIndex);
            String imagePath = parentDirectory + imageModel.getFileName();
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
                System.out.println("ImageViewerComponent.showImage: currentImage "+image);
                if (isForSegmentsView) {
                    setCurrentThumbnail((Thumbnail) imageModel);

                    // Ustal rozmiar segmentPane na podstawie rozmiaru wczytanego obrazu
                    segmentPane.setMinWidth(imageWidth);
                    segmentPane.setMinHeight(imageHeight);
                    segmentPane.setMaxWidth(imageWidth);
                    segmentPane.setMaxHeight(imageHeight);
                    segmentPane.setOnMouseClicked(mouseEvent -> {
                        highlightedPolygon = null;
                    });

                    // załadowanie segmentów
                    loadSegments();
                }
                else {
                    Photo photo = new Photo(imageModel.getId(), imageModel.getFileName(), imageModel.getParentId());
                    setCurrentPhoto(photo);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
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

                // przekaz biezacy segment do słuchacza
                segmentsListener.onCurrentSegmentChanged(segment);
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

    @Override
    public void onDeleteConfirmed(ActionEvent event, Object view) {
        if (isForSegmentsView)
            System.out.println("ImageViewerComponent.onDeleteConfirmed: "+currentThumbnail.toString());
        else {
            System.out.println("Potwierdzenie usuniecia zdjecia");
//            setCurrentPhoto((Photo) currentThumbnail);
        }
        // usuniecie z bazy
        // thumbnails
        if (isForSegmentsView) {
            try {
                if (new ThumbnailOperations().deleteThumbnailWithSubstructures(currentThumbnail.getId()))
                    System.out.println("usunieto miniature z bazy");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        // photos
        else {
            try {
                ImageModel current = images.get(currentImageIndex);
                if (new PhotoOperations().deletePhoto(current.getId()))
                    System.out.println("usunięto zdjęcie z bazy");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // usuniecie pliku
        if (isForSegmentsView)
            new DirectoryOperator().removeStructure(currentThumbnail, parentDirectory);
        else
            new DirectoryOperator().removeStructure(currentPhoto, parentDirectory);

        currentImageIndex++;
        try {
            showImage(currentImageIndex);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
