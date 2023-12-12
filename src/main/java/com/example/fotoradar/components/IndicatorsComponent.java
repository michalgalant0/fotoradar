package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class IndicatorsComponent extends VBox {
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label sizeLabel;

    public IndicatorsComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/IndicatorsComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public void initialize() {
    }

    public void setProgress(double value) {
        progressBar.setProgress(value / 100.0);
    }

    public void setSize(double value) {
        sizeLabel.setText(String.format("%.2f MB", value));
    }

    public void addWaveAnimation() {
        // Animacja paska postępu
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progressBar.progressProperty(), progressBar.getProgress()))
        );
        timeline.play();
    }
}
