package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;

public class Collectibles extends AnchorPane {

    @FXML
    private HBox collectiblesContainer;

    private ArrayList<Collectible> collectibles;

    public Collectibles() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/Collectibles.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void setCollectibles(ArrayList<Collectible> collectibles) {
        this.collectibles = collectibles;
    }

    public void fillCollectiblesHBox() {
        for (Collectible collectible : collectibles) {
            // utworzenie komponentu obiektu
            com.example.fotoradar.components.Collectible collectibleComponent =
                    new com.example.fotoradar.components.Collectible();
            // ustawienie tytułu komponentu
            collectibleComponent.setHeaderLabel(collectible.getTitle());
            // ustawienie listy obiektów kolekcji
            collectibleComponent.setCollectible(collectible);

            collectiblesContainer.getChildren().add(collectibleComponent);
        }
    }
}
