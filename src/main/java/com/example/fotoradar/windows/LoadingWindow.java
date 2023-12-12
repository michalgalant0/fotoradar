package com.example.fotoradar.windows;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadingWindow {
    private Stage stage;
    private Label loadingLabel;
    private ProgressBar progressBar;
    private AnimationThread animationThread;

    public LoadingWindow() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);

        loadingLabel = new Label("Ładowanie zdjęć");
        progressBar = new ProgressBar(0.0);

        VBox vbox = new VBox(16);
        vbox.getChildren().addAll(loadingLabel, progressBar);
        vbox.setAlignment(Pos.CENTER);

        StackPane layout = new StackPane(vbox);
        layout.setPadding(new javafx.geometry.Insets(20, 20, 20, 20)); // Dodaj odstęp od góry, prawej, dołu i lewej

        Scene scene = new Scene(layout, 250, 100);
        stage.setScene(scene);

        animationThread = new AnimationThread();
    }

    public void showLoading() {
        stage.show();
        stage.toFront();
        stage.requestFocus();
        animationThread.start();
    }

    public void hideLoading() {
        animationThread.stopThread();
        stage.hide();
        stage.close();
    }

    public void updateLoading(String message, double progress) {
        loadingLabel.setText(message);
        progressBar.setProgress(progress);
    }

    private class AnimationThread extends Thread {
        private volatile boolean stop = false;

        public void stopThread() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                try {
                    Platform.runLater(() -> {
                        String text = loadingLabel.getText();
                        if (text.endsWith("...")) {
                            loadingLabel.setText(text.substring(0, text.length() - 3));
                        } else {
                            loadingLabel.setText(text + ".");
                        }
                    });
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
