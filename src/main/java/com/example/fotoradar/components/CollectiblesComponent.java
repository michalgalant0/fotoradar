package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

public class CollectiblesComponent extends AnchorPane {

    @FXML
    private HBox collectiblesContainer;
    @Setter
    private ArrayList<Collectible> collectibles;

    public CollectiblesComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectiblesComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillCollectiblesHBox() {
        for (Collectible collectible : collectibles) {
            // utworzenie komponentu obiektu
            CollectibleComponent collectibleComponent =
                    new CollectibleComponent();
            // ustawienie tytułu komponentu
            collectibleComponent.setHeaderLabel(collectible.getTitle());
            // ustawienie listy obiektów kolekcji
            collectibleComponent.setCollectible(collectible);

            collectiblesContainer.getChildren().add(collectibleComponent);
        }
    }
}