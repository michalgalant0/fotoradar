package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Collections extends AnchorPane {

    @FXML
    private HBox collectionsContainer;

    private ArrayList<com.example.fotoradar.models.Collection> collections;
    private CollectibleOperations collectibleOperations = new CollectibleOperations();

    public Collections() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/Collections.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }


    public void setCollections(ArrayList<Collection> collections) {
        this.collections = collections;
    }

    private void fillCollectionsHBox() throws SQLException, IOException {
        for (Collection collection : collections) {
            // utworzenie komponentu kolekcja
            com.example.fotoradar.components.Collection collectionComponent =
                    new com.example.fotoradar.components.Collection();
            // ustawienie tytułu komponentu
            collectionComponent.setHeaderLabel(collection.getTitle());
            // ustawienie listy obiektów kolekcji
            ArrayList<Collectible> collectibles = collectibleOperations.getAllCollectibles(collection.getId());
            collectionComponent.setCollectibles(collectibles);

            collectionsContainer.getChildren().add(collectionComponent);
        }
    }
}
