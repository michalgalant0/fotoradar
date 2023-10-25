package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectiblesComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
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
    private void goToParameters(ActionEvent event) throws IOException {
        System.out.println("przejscie do parametrow kolekcji");
        new SwitchScene().switchScene(event, "parametersView");
    }
    @FXML
    private void addCollectible(ActionEvent event) throws IOException {
        System.out.println("dodaj obiekt");
        new SwitchScene().displayWindow("CollectibleFormWindow", "Dodaj obiekt");
    }
    @FXML
    private void removeCollection(ActionEvent event) throws IOException {
        System.out.println("usun kolekcje");
        new SwitchScene().displayWindow("ConfirmDeletePopup", "Potwierdź usuwanie");
    }
    @FXML
    private void backToCollections(ActionEvent event) throws IOException {
        System.out.println("powrot do kolekcji");
        new SwitchScene().switchScene(event, "collectionsView");
    }
}
