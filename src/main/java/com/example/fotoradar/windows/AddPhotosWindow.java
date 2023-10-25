package com.example.fotoradar.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.util.List;

public class AddPhotosWindow implements Window {
    @FXML
    public Label counter;
    @Setter
    private Stage dialogStage;
    private List<File> selectedFiles;

    @FXML
    public void addPhotos(ActionEvent event) {
        System.out.println("dodanie zdjęć");
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anulowanie");
        closeWindow(dialogStage);
    }

    @FXML
    public void pickFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        selectedFiles = fileChooser.showOpenMultipleDialog(counter.getScene().getWindow());

        if (selectedFiles != null) {
            displaySelectedFiles();
        }
    }

    private void displaySelectedFiles() {
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            return;
        }

        StringBuilder fileNames = new StringBuilder();
        for (File file : selectedFiles) {
            fileNames.append(file.getName()).append("\n");
        }

        counter.setText(fileNames.toString() + "Ilość załadowanych: " + selectedFiles.size());
    }
}
