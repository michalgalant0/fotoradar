package com.example.fotoradar.views;

import com.example.fotoradar.components.CollectiblesComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import javafx.fxml.FXML;

import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionView {
    @FXML
    public CollectiblesComponent collectiblesComponent;
    private ArrayList<Collectible> collectibles = new ArrayList<>();

    public void initialize() throws SQLException {
        // tutaj pobranie z bazy testowo, normalnie przekazanie z poprzedniego modułu
        collectiblesComponent.setCollectibles(
                new CollectibleOperations().getAllCollectibles(8)
        );
        collectiblesComponent.fillCollectiblesHBox();
    }

    @FXML
    private void goToParameters() {
        System.out.println("przejscie do parametrow kolekcji");
    }
    @FXML
    private void addCollectible() {
        System.out.println("dodaj obiekt");
    }
    @FXML
    private void removeCollection() {
        System.out.println("usun kolekcje");
    }
    @FXML
    private void backToCollections() {
        System.out.println("powrot do kolekcji");
    }
}
