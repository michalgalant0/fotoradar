package com.example.fotoradar.controllers.views;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.controllers.components.CollectionsComponent;
import com.example.fotoradar.controllers.contexts.CollectionsViewContext;
import javafx.fxml.FXML;

import java.io.IOException;

public class CollectionsView implements View {
    @FXML
    public CollectionsComponent collectionsComponent;

    private CollectionsViewContext collectionsViewContext;

    public CollectionsView() {
        collectionsViewContext = new CollectionsViewContext();
    }

    public void initialize() {
        collectionsComponent.setCollections(context.getCollections());
        collectionsComponent.setCollectibles(context.getCollectibles());
        collectionsComponent.setThumbnails(collectionsViewContext.getThumbnails());

        try {
            collectionsComponent.fillCollectionsHBox();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // utworzenie katalogu KOLEKCJE jesli nie istnieje
        new DirectoryOperator().createStructure();
    }

    @FXML
    private void addCollection() throws IOException {
        System.out.println("dodaj kolekcje");
        new SwitchScene().displayWindow("CollectionFormWindow", "Dodaj kolekcję");
    }
}
