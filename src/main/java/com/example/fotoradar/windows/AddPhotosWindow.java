package com.example.fotoradar.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.util.List;

public class AddPhotosWindow implements Window {
    @FXML
    public TextArea fileNamesContainer;
    @FXML
    public Label counterLabel;
    @FXML
    public Button addPhotoButton;
    @FXML
    public Button cancelButton;
    @Setter
    private Stage dialogStage;
    private List<File> selectedFiles;

    @FXML
    public void addPhotos(ActionEvent event) {
        System.out.println("lista dodanych zdjęć:");
        System.out.println(selectedFiles);

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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"));
        selectedFiles = fileChooser.showOpenMultipleDialog(counterLabel.getScene().getWindow());

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

        fileNamesContainer.setText(fileNames.toString());
        counterLabel.setText("ilość wczytanych plików: " + selectedFiles.size());

        // Pokaż wczytane elementy, ustawiając visible i managed na true
        fileNamesContainer.setVisible(true);
        fileNamesContainer.setManaged(true);
        counterLabel.setVisible(true);
        counterLabel.setManaged(true);
        addPhotoButton.setVisible(true);
        cancelButton.setVisible(true);

        // Dostosuj wielkość sceny (stage)
        Stage stage = (Stage) fileNamesContainer.getScene().getWindow();
        stage.setWidth(300);  // Ustaw preferowaną szerokość
        stage.setHeight(400); // Ustaw preferowaną wysokość
    }
}
