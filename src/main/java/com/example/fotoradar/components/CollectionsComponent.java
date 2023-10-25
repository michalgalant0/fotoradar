package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollectionsComponent extends AnchorPane {

    @FXML
    private HBox collectionsContainer;

    @Setter
    private ArrayList<Collection> collections;
    @Setter
    private Map<Integer, ArrayList<Collectible>> collectiblesMap;

    public CollectionsComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectionsComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillCollectionsHBox() throws IOException {
        for (Collection collection : collections) {
            // utworzenie komponentu kolekcja
            CollectionComponent collectionComponent =
                    new CollectionComponent();
            // ustawienie tytułu komponentu
            collectionComponent.setHeaderLabel(collection.getTitle());
            // ustawienie listy obiektów kolekcji
            ArrayList<Collectible> collectibles = collectiblesMap.get(collection.getId());
            if (collectibles != null)
                collectionComponent.setCollectibles(collectibles);

            collectionsContainer.getChildren().add(collectionComponent);
        }
    }
}
