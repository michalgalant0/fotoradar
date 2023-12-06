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

    private static String createPropertiesFile(String value) {
        Properties properties = new Properties();

        try (FileOutputStream fileOutputStream = new FileOutputStream("properties")) {
            properties.setProperty("defaultPath", value);
            Main.setDefPath(value);
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
        try (FileInputStream fileInputStream = new FileInputStream("properties")) {
            properties.load(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            return properties.getProperty("defaultPath");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void initializeDefPath() {
        File propertiesFile = new File("properties");

        if (propertiesFile.isFile()) {
            String defaultPath = readDefaultPathFromProperties();

            if (defaultPath != null) {
                setDefPath(defaultPath);
                return;
            }
        }
        // Jeśli plik nie istnieje
        setDefPath(createPropertiesFile(System.getProperty("user.dir")));
        //startowa scieżka gdy uruchamiamy aplikacje po raz pierwszy
        System.out.println(getDefPath());
    }
}