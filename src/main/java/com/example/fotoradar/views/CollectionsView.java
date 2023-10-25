package com.example.fotoradar.views;

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

    private ArrayList<Collection> collections = new ArrayList<>();
    private Map<Integer, ArrayList<Collectible>> collectiblesMap = new HashMap<>();

    public void initialize() throws SQLException, IOException {
        CollectionOperations collectionOperations = new CollectionOperations();
        collections = collectionOperations.getAllCollections();

        CollectibleOperations collectibleOperations = new CollectibleOperations();
        for (Collection collection : collections) {
            int collectionId = collection.getId();
            collectiblesMap.put(collectionId, collectibleOperations.getAllCollectibles(collectionId));
        }

        collectionsComponent.setCollections(collections);
        collectionsComponent.setCollectiblesMap(collectiblesMap);

        collectionsComponent.fillCollectionsHBox();
    }

    @FXML
    private void addCollection() {
        System.out.println("dodaj kolekcje");
    }
}
