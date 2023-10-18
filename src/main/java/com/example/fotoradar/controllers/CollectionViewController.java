package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Getter @Setter
public class CollectionViewController {
    public Label titleLabel;
    public ListView<Collectible> collectiblesListView;
    public Button backToCollectionsButton;
    public Button addCollectibleButton;
    public Button editCollectionButton;
    public Button deleteCollectionButton;

    private Collection collection;

    @FXML
    public void initialize() throws SQLException {
        titleLabel.setText("kolekcje/ "+collection.getTitle());
        fillListView();
    }

    private void fillListView() throws SQLException {
        ArrayList<Collectible> collectibles = new CollectibleOperations().getAllCollectibles(collection.getId());

        for (Collectible collectible : collectibles)
            collectiblesListView.getItems().add(collectible);
    }

    @FXML
    private void backToCollections (ActionEvent event) throws IOException {
        new SwitchScene(event, "collections-view");
    }

    @FXML
    private void addCollectible (ActionEvent event) throws IOException, SQLException {
        AddCollectibleViewController controller = new AddCollectibleViewController();
        controller.setCollection(this.collection);

        new SwitchScene(event, "add_collectible_view", controller);
    }


    public void editCollection(ActionEvent event) throws IOException, SQLException {
        // Tworzenie kontrolera dla widoku edycji kolekcji i przekazywanie parametrów kolekcji
        EditCollectionViewController controller = new EditCollectionViewController();
        controller.setCollection(getCollection());

        // Przenoszenie do widoku kolekcji z przekazaną kolekcją
        new SwitchScene(event, "edit_collection_view", controller);
    }

    @FXML
    private void deleteCollection(ActionEvent event) throws IOException, SQLException {
        CollectionOperations collectionOperations = new CollectionOperations();
        boolean isDeleted = collectionOperations.deleteCollection(collection.getId());

        if (isDeleted) {
            // Po usunięciu -> powrót do widoku listy kolekcji
            new SwitchScene(event, "collections-view");
        } else {
            // Obsługa przypadku, gdy nie udało się usunąć kolekcji
        }
    }

}
