package com.example.fotoradar.windows;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadingWindow {
    private Stage stage;
    private Label loadingLabel;
    private AnimationThread animationThread;

    public LoadingWindow() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        loadingLabel = new Label("Ładowanie zdjęć");
        animationThread = new AnimationThread();

        StackPane layout = new StackPane(loadingLabel);
        Scene scene = new Scene(layout, 350, 100);
        stage.setScene(scene);
    }

    public void showLoading() {
        stage.show();
        animationThread.start();
    }

    public void hideLoading() {
        animationThread.stopThread();
        stage.hide();
        stage.close();
    }

    public void updateLoading(String message) {
        loadingLabel.setText(message);
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
