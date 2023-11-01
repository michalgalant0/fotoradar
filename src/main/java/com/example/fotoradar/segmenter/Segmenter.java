package com.example.fotoradar.segmenter;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Segmenter extends Application {
    private Stage primaryStage;
    @Getter
    private ArrayList<Segment> segments = new ArrayList<>();
    private Segment currentSegment;
    private Canvas canvas;
    private double previousX;
    private double previousY;

    @Setter
    private SegmenterListener segmenterListener;

    @Setter
    private Image currentImage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Segment Drawer");
        BorderPane root = new BorderPane();

        canvas = new Canvas(Math.min(800, currentImage.getWidth()), Math.min(800, currentImage.getHeight()));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(currentImage, 0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.setOnMouseClicked(event -> {
            if (currentSegment == null) {
                currentSegment = new Segment();
                currentSegment.addPoint(event.getX(), event.getY());
                previousX = event.getX();
                previousY = event.getY();
            } else if (currentSegment.getPoints().size() < 8) {
                currentSegment.addPoint(event.getX(), event.getY());
                previousX = event.getX();
                previousY = event.getY();
            }

            if (currentSegment.getPoints().size() == 8) {
                segments.add(currentSegment);
                currentSegment = null;
                printSegments();
                redrawCanvas();
            } else {
                drawSegmentPoints(); // Dodaj rysowanie punktów segmentu w trakcie rysowania
            }
        });

        canvas.setOnMouseMoved(event -> {
            if (currentSegment != null && !currentSegment.getPoints().isEmpty()) {
                double currentX = event.getX();
                double currentY = event.getY();
                redrawCanvas();
                drawSegmentPoints();
                drawDynamicLine(previousX, previousY, currentX, currentY);
            }
        });

        root.setCenter(canvas);

        Button segmentButton = new Button("ZAKOŃCZ SEGMENTOWANIE");
        segmentButton.setOnAction(event -> {
            endSegmenting();
            segmenterListener.onSegmentationFinished(segments);
        });

        VBox buttonBox = new VBox(segmentButton);
        buttonBox.setAlignment(Pos.CENTER);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 800, 840);
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

        for (Segment segment : segments) {
            List<Double> points = segment.getPoints();
            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeLine(points.get(0), points.get(1), points.get(2), points.get(3));
            gc.strokeLine(points.get(2), points.get(3), points.get(4), points.get(5));
            gc.strokeLine(points.get(4), points.get(5), points.get(6), points.get(7));
            gc.strokeLine(points.get(6), points.get(7), points.get(0), points.get(1));
        }
    }

    private void drawSegmentPoints() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        for (int i = 0; i < currentSegment.getPoints().size(); i += 2) {
            double x = currentSegment.getPoints().get(i);
            double y = currentSegment.getPoints().get(i + 1);
            gc.fillOval(x - 2, y - 2, 4, 4);
        }
    }

    private void drawDynamicLine(double startX, double startY, double endX, double endY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
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
