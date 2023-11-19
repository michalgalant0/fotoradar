package com.example.fotoradar.views;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectionsComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectionsView {
    @FXML
    public CollectionsComponent collectionsComponent;

    private CollectionOperations collectionOperations;
    private CollectibleOperations collectibleOperations;
    private ArrayList<Collection> collections;
    private Map<Integer, ArrayList<Collectible>> collectiblesMap;

    public CollectionsView() {
        try {
            collectionOperations = new CollectionOperations();
            collectibleOperations = new CollectibleOperations();
            collections = new ArrayList<>();
            collectiblesMap = new HashMap<>();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() throws SQLException, IOException {
        collections = collectionOperations.getAllCollections();
        collectiblesMap = new HashMap<>();
        for (Collection collection : collections) {
            int collectionId = collection.getId();
            collectiblesMap.put(collectionId, collectibleOperations.getAllCollectibles(collectionId));
        }
        collectionsComponent.setCollections(collections);
        collectionsComponent.setCollectiblesMap(collectiblesMap);
        collectionsComponent.fillCollectionsHBox();

        // utworzenie katalogu KOLEKCJE jesli nie istnieje
        DirectoryOperator.getInstance().createStructure();
    }

    @FXML
    private void addCollection() throws IOException {
        System.out.println("dodaj kolekcje");
        new SwitchScene().displayWindow("CollectionFormWindow", "Dodaj kolekcję");
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refresh() throws SQLException, IOException {
        collections = collectionOperations.getAllCollections();
        collectiblesMap = new HashMap<>();
        for (Collection collection : collections) {
            int collectionId = collection.getId();
            collectiblesMap.put(collectionId, collectibleOperations.getAllCollectibles(collectionId));
        }
        collectionsComponent.setCollections(collections);
        collectionsComponent.setCollectiblesMap(collectiblesMap);
        collectionsComponent.fillCollectionsHBox();
    }
}
