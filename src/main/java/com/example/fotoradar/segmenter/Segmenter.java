package com.example.fotoradar.segmenter;

import com.example.fotoradar.Main;
import com.example.fotoradar.Palette;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Segmenter extends Application {
    private Stage primaryStage;
    @Getter
    private ArrayList<Segment> segments = new ArrayList<>();
    @Setter
    private ArrayList<Segment> segmentsToShow;
    private Segment currentSegment;
    private Canvas canvas;
    private double previousX;
    private double previousY;

    @Setter
    private SegmenterListener segmenterListener;

    @Setter
    private Image currentImage;
    @Setter
    private int currentThumbnailId;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Segmenter");
        BorderPane root = new BorderPane();

        canvas = new Canvas(Math.min(800, currentImage.getWidth()), Math.min(800, currentImage.getHeight()));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double offsetX = (canvas.getWidth() - currentImage.getWidth()) / 2;
        double offsetY = (canvas.getHeight() - currentImage.getHeight()) / 2;
        gc.drawImage(currentImage, offsetX, offsetY);

        // narysowanie wczesniejszych segmentow
        if (!segmentsToShow.isEmpty())
            redrawCanvas();

        canvas.setOnMouseClicked(event -> {
            if (currentSegment == null) {
                currentSegment = new Segment();
                currentSegment.addPoint(event.getX() - offsetX, event.getY() - offsetY);
                previousX = event.getX();
                previousY = event.getY();
            } else if (currentSegment.getPoints().size() < 8) {
                currentSegment.addPoint(event.getX() - offsetX, event.getY() - offsetY);
                previousX = event.getX();
                previousY = event.getY();
            }

            if (currentSegment.getPoints().size() == 8) {
                segments.add(currentSegment);
                currentSegment = null;
                printSegments();
                redrawCanvas();
            } else {
                drawSegmentPoints();
            }
        });

        canvas.setOnMouseMoved(event -> {
            if (currentSegment != null && !currentSegment.getPoints().isEmpty()) {
                double currentX = event.getX() - offsetX;
                double currentY = event.getY() - offsetY;
                redrawCanvas();
                drawSegmentPoints();
                drawDynamicLine(previousX - offsetX, previousY - offsetY, currentX, currentY);
            }
        });

        root.setCenter(canvas);

        Button segmentButton = new Button("ZAKOŃCZ SEGMENTOWANIE");
        segmentButton.setOnAction(event -> {
            endSegmenting();
            try {
                segmenterListener.onSegmentationFinished(segments, currentThumbnailId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        VBox buttonBox = new VBox(segmentButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(16, 0, 16, 0));
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 800, 864);
        scene.getStylesheets().add(Main.class.getResource("styles/windows/SegmenterStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void endSegmenting() {
        if (currentSegment != null) {
            segments.add(currentSegment);
            currentSegment = null;
            printSegments();
            redrawCanvas();
        }
        // Close the Segmenter window
        primaryStage.close();
    }

    private void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(currentImage, 0, 0, canvas.getWidth(), canvas.getHeight());

        for (Segment segment : segments)
            drawSegment(gc, segment);

        // narysowanie wczesniej istniejacych segmentow
        for (Segment segment : segmentsToShow)
            drawSegment(gc, segment);
    }

    private void drawSegment(GraphicsContext gc, Segment segment) {
        List<Double> points = segment.getPoints();
        gc.setStroke(Palette.MAIN.getColor());
        gc.setLineWidth(3);
        gc.strokeLine(points.get(0), points.get(1), points.get(2), points.get(3));
        gc.strokeLine(points.get(2), points.get(3), points.get(4), points.get(5));
        gc.strokeLine(points.get(4), points.get(5), points.get(6), points.get(7));
        gc.strokeLine(points.get(6), points.get(7), points.get(0), points.get(1));
    }

    private void drawSegmentPoints() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Palette.SECONDARY.getColor());
        for (int i = 0; i < currentSegment.getPoints().size(); i += 2) {
            double x = currentSegment.getPoints().get(i);
            double y = currentSegment.getPoints().get(i + 1);
            gc.fillOval(x - 2, y - 2, 4, 4);
        }
    }

    private void drawDynamicLine(double startX, double startY, double endX, double endY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Palette.SECONDARY.getColor());
        gc.setLineWidth(2);
        gc.strokeLine(startX, startY, endX, endY);
    }

    private void printSegments() {
        System.out.println("Lista segmentów:");
        for (Segment segment : segments) {
            System.out.println(segment.toString());
        }
    }

    public static class Segment {
        private ArrayList<Double> points = new ArrayList<>();

        public void addPoint(double x, double y) {
            points.add(x);
            points.add(y);
        }

        public List<Double> getPoints() {
            return points;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < points.size(); i += 2) {
                sb.append("(").append(points.get(i)).append(",").append(points.get(i + 1)).append(") ");
            }
            return sb.toString();
        }
    }
}
