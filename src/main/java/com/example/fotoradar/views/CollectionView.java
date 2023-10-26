package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectiblesComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionView {
    @FXML
    private Label windowLabel;
    @FXML
    private CollectiblesComponent collectiblesComponent;

    @Setter
    private Collection collection = new Collection();

    public void initialize() throws SQLException, IOException {
        System.out.println("CollectionView.initialize: "+collection);
        // ustawienie nagłówka okna
        setWindowLabel(collection.getTitle());
        // wypełnienie listy obiektów danymi pobranymi z bazy
        ArrayList<Collectible> collectibles =
                new CollectibleOperations().getAllCollectibles(collection.getId());
        collectiblesComponent.setCollectibles(collectibles);
        collectiblesComponent.fillCollectiblesHBox();
    }

    private void setWindowLabel(String collectionName) {
        windowLabel.setText("kolekcje/ "+collectionName);
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
