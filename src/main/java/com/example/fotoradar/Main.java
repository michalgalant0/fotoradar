package com.example.fotoradar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Main extends Application {
    @Getter @Setter
    private static String defPath;

    @Override
    public void start(Stage stage) throws IOException {
        // main view
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/collectionsView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("fotoradar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        initializeDefPath();
        launch();
    }

    private static String createPropertiesFile(String fileName, String key, String value) {
        Properties properties = new Properties();

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            properties.setProperty(key, "" + value + "");
            Main.setDefPath(value);
            properties.store(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8), "Default Path");
            System.out.println("Properties file created successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error creating properties file: " + ex.getMessage());
        }
        return readDefaultPathFromProperties("properties", "defaultPath");
    }

    private static String readDefaultPathFromProperties(String fileName, String key) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void initializeDefPath() {
        File propertiesFile = new File("properties");

        if (propertiesFile.isFile()) {
            String defaultPath = readDefaultPathFromProperties("properties", "defaultPath");

            if (defaultPath != null) {
                setDefPath(defaultPath);
                return;
            }
        }
        // Jeśli plik nie istnieje
        setDefPath(createPropertiesFile("properties", "defaultPath", System.getProperty("user.dir")));
        //startowa scieżka gdy uruchamiamy aplikacje po raz pierwszy
        System.out.println(getDefPath());
    }
}