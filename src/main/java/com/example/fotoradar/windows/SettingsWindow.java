package com.example.fotoradar.windows;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;


public class SettingsWindow implements Window{
    private static final String PROPERTIES_FILE_NAME = "target/classes/properties";

    @Setter
    private Stage dialogStage;
    private Stage stage;
    private String newPath;
    private String newPathDecoded;

    @FXML
    private Button CancelButton;
    @FXML
    private Button SaveSettingsButton;
    @FXML
    private Button ChangeDirectoryButton;
    @FXML
    private Label pathLabel;
    @FXML
    private void Cancel(){
        cancel();
    }
    @FXML
    private void SaveSettings() throws UnsupportedEncodingException {saveSettings();}
    @FXML
    private void ChangeDirectory() throws UnsupportedEncodingException {
        changeDirectory();
        // Po wyborze nowej ścieżki, sprawdź czy ścieżka została zmieniona, aby aktywować przycisk "ZASTOSUJ"
        if (newPathDecoded != null && !newPathDecoded.isEmpty()) {
            SaveSettingsButton.setDisable(false); // Aktywuj przycisk "ZASTOSUJ"
        }
    }

    public void initialize(){
        pathLabel.setText(readDefaultPathFromProperties(PROPERTIES_FILE_NAME, "defaultPath"));
        SaveSettingsButton.setDisable(true);
    }

    private String readDefaultPathFromProperties(String fileName, String key) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updatePropertiesFile(String fileName, String key, String value) {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            properties.setProperty(key, value);

            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                properties.store(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8), "Updated Default Path");
                System.out.println("Properties file updated successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error updating properties file: " + ex.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading properties file: " + e.getMessage());
        }
    }

    private void changeDirectory() throws UnsupportedEncodingException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        File selectedDirectory = directoryChooser.showDialog(stage);
        newPath = selectedDirectory.getAbsolutePath();
        newPathDecoded = URLDecoder.decode(newPath, "UTF-8");
        pathLabel.setText(newPathDecoded);


    }

    private void moveFolder(String oldPath, String newPath, String folderName) {
        Path source = Paths.get(oldPath, folderName);
        Path destination = Paths.get(newPath, folderName);

        if (Files.exists(source) && Files.isDirectory(source)) {
            try {
                Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Path target = destination.resolve(source.relativize(file));
                        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        Path target = destination.resolve(source.relativize(dir));
                        Files.createDirectories(target);
                        return FileVisitResult.CONTINUE;
                    }
                });
                Files.walkFileTree(source, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                System.out.println("Folder " + folderName + " moved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error moving folder " + folderName + ": " + e.getMessage());

                // Logowanie błędów
                java.util.logging.Logger logger = java.util.logging.Logger.getLogger("FolderMoveLogger");
                logger.log(java.util.logging.Level.SEVERE, "Error moving folder " + folderName, e);
            }
        }
    }


    private void saveSettings() throws UnsupportedEncodingException {
        if (newPathDecoded != null) {

            String oldPath = readDefaultPathFromProperties(PROPERTIES_FILE_NAME, "defaultPath");

            if (oldPath != null) {
                // Update the displayed path label
                pathLabel.setText(newPathDecoded);

                // Move the folders if they exist
                moveFolder(oldPath, newPathDecoded, "KOLEKCJE");
                moveFolder(oldPath, newPathDecoded, "RAPORTY");
            }
        }
        // Ustaw nową ścieżkę jako domyślną
        updatePropertiesFile(PROPERTIES_FILE_NAME, "defaultPath", newPathDecoded);
        Main.setDefPath(readDefaultPathFromProperties(PROPERTIES_FILE_NAME, "defaultPath"));
        SaveSettingsButton.setDisable(true);
        //closeWindow(dialogStage);
    }

    private void cancel() {
        closeWindow(dialogStage);
    }
}



