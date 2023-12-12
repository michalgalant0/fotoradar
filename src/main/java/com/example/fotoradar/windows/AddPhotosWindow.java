package com.example.fotoradar.windows;

import com.example.fotoradar.AddPhotoListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    @Setter
    private AddPhotoListener addPhotoListener;
    @Setter
    private OnWindowClosedListener onWindowClosedListener;

    @FXML
    public void addPhotos(ActionEvent event) throws IOException, SQLException {
        System.out.println("lista dodanych zdjęć:");
        System.out.println(selectedFiles);

        closeWindow(dialogStage);

        addPhotoListener.onAddingPhotosFinished(selectedFiles);
        onWindowClosedListener.onWindowClosed();
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anulowanie");
        closeWindow(dialogStage);
    }

    @FXML
    public void pickFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg", "*.raw")
        );

        List<File> selectedFilesList = fileChooser.showOpenMultipleDialog(counterLabel.getScene().getWindow());

        if (selectedFilesList != null && !selectedFilesList.isEmpty()) {
            selectedFiles = selectedFilesList;
            displaySelectedFiles();
        }
    }

    @FXML
    public void pickFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(counterLabel.getScene().getWindow());

        if (selectedDirectory != null && selectedDirectory.isDirectory()) {
            // Pobierz wszystkie pliki obrazów w folderze (bez skanowania podfolderów)
            selectedFiles = Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles((dir, name) ->
                    name.trim().toLowerCase().endsWith(".jpg") ||
                            name.trim().toLowerCase().endsWith(".png") ||
                            name.trim().toLowerCase().endsWith(".jpeg") ||
                            name.trim().toLowerCase().endsWith(".raw"))));

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
