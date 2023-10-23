package com.example.fotoradar.controllers;

import com.example.fotoradar.Main;
import com.example.fotoradar.components.MiniGallery;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestViewController {

    @FXML
    public MiniGallery miniGallery;

    public TestViewController() {
    }

    public void initialize() throws IOException, URISyntaxException {

        // pobranie sciezek do plikow w katalogu tmpPhotos
        String directoryName = "tmpPhotos";
        Path directoryPath = Paths.get(
                Objects.requireNonNull(Main.class.getClassLoader().getResource(directoryName)).toURI()
        );
        ArrayList<String> imagePaths = Files.walk(directoryPath, 1)
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toCollection(ArrayList::new));

        miniGallery.setImagePaths(imagePaths);
        miniGallery.initialize();
    }

}
