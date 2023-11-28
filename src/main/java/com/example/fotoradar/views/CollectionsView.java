package com.example.fotoradar.views;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectionsComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.windows.SettingsWindow;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectionsView {
    @FXML
    public CollectionsComponent collectionsComponent;

    public void initialize() throws SQLException, IOException {
        CollectionOperations collectionOperations = new CollectionOperations();
        ArrayList<Collection> collections = collectionOperations.getAllCollections();

        CollectibleOperations collectibleOperations = new CollectibleOperations();
        Map<Integer, ArrayList<Collectible>> collectiblesMap = new HashMap<>();
        for (Collection collection : collections) {
            int collectionId = collection.getId();
            collectiblesMap.put(collectionId, collectibleOperations.getAllCollectibles(collectionId));
        }

        collectionsComponent.setCollections(collections);
        collectionsComponent.setCollectiblesMap(collectiblesMap);

        collectionsComponent.fillCollectionsHBox();

        // utworzenie katalogu KOLEKCJE jesli nie istnieje
        new DirectoryOperator().createStructure();
    }

    @FXML
    private void addCollection() throws IOException, SQLException {
        System.out.println("dodaj kolekcje");
        new SwitchScene().displayWindow("CollectionFormWindow", "Dodaj kolekcję");
        initialize();
    }

    @FXML
    private void openSettings() throws IOException{
        System.out.println("wejscie w ustawienia");
        SettingsWindow settingsWindow = new SettingsWindow();
        new SwitchScene().displayWindow("settingsWindow", "USTAWIENIA", settingsWindow);
    }
}
