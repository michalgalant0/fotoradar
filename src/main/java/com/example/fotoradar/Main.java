package com.example.fotoradar;

import com.example.fotoradar.databaseOperations.StatusManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class Main extends Application {
    @Getter @Setter
    private static String defPath;

    private static final String PROPERTIES_FILE_NAME = "target/classes/properties";

    @Override
    public void start(Stage stage) throws IOException {
        // main view
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/collectionsView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("fotoradar");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println(StatusManager.getInstance().getStatuses());
        initializeDefPath();
        launch();
    }

    private static String createPropertiesFile(String value) {
        Properties properties = new Properties();

        try (FileOutputStream fileOutputStream = new FileOutputStream(PROPERTIES_FILE_NAME)) {
            properties.setProperty("defaultPath", value);
            setDefPath(value);
            properties.store(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8), "Default Path");
            System.out.println("Properties file created successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error creating properties file: " + ex.getMessage());
        }
        return readDefaultPathFromProperties();
    }

    private static String readDefaultPathFromProperties() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE_NAME)) {
            properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            return properties.getProperty("defaultPath");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void initializeDefPath() {
        File propertiesFile = new File(PROPERTIES_FILE_NAME);

        if (propertiesFile.isFile()) {
            String defaultPath = readDefaultPathFromProperties();

            if (defaultPath != null) {
                setDefPath(defaultPath);
                return;
            }
        }
        // Jeśli plik nie istnieje
        setDefPath(createPropertiesFile(System.getProperty("user.dir")));
    }
}