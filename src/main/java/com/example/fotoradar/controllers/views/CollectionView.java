package com.example.fotoradar.controllers.views;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.controllers.components.CollectiblesComponent;
import com.example.fotoradar.controllers.contexts.CollectionViewContext;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.controllers.windows.CollectibleFormWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionView implements View {
    @FXML
    private Label windowLabel;
    @FXML
    private CollectiblesComponent collectiblesComponent;

    private CollectionViewContext collectionViewContext;

    private Collection currentCollection;

    public CollectionView() {
        collectionViewContext = new CollectionViewContext();
        currentCollection = collectionViewContext.getCurrentCollection();
        System.out.println(currentCollection);
    }

    public void initialize() throws IOException {
        System.out.println("CollectionView.initialize: "+currentCollection);
        // ustawienie nagłówka okna
        setWindowLabel(currentCollection.getTitle());
        collectiblesComponent.setCollectibles(collectionViewContext.getCollectibles());
        collectiblesComponent.setThumbnails(collectionViewContext.getThumbnails());
        collectiblesComponent.fillCollectiblesHBox();

        new DirectoryOperator().createStructure(currentCollection);
    }

    private void setWindowLabel(String collectionName) {
        windowLabel.setText("kolekcje/ "+collectionName);
    }

    @FXML
    private void goToParameters(ActionEvent event) throws IOException {
        System.out.println("przejscie do parametrow kolekcji");
        System.out.println("CollectionView.goToParameters: "+currentCollection);
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
