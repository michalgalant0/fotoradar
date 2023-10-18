package com.example.fotoradar.controllers;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

@Getter @Setter
public class CollectionViewController {
    public Label titleLabel;
    public ListView<Collectible> collectiblesListView;
    public Button backToCollectionsButton;
    public Button addCollectibleButton;
    public Button editCollectionButton;

    private Collection collection;

    @FXML
    public void initialize() {
        titleLabel.setText("kolekcje/ "+collection.getTitle());
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

    public void editCollection(ActionEvent event) throws IOException {
        //tmp
        new SwitchScene(event, "edit_collection_view");
    }
}
