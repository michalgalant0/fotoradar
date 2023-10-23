package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;

public class Collectible extends AnchorPane {

    @FXML
    public Label headerLabel;
    @FXML
    public TextField startDateTextField;
    @FXML
    public TextField finishDateTextField;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public TextField statusTextField;

    private com.example.fotoradar.models.Collectible collectible;

    public Collectible() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/Collectible.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCollectible(com.example.fotoradar.models.Collectible collectible) {
        this.collectible = collectible;
        fillComponent();
    }

    private void fillComponent() {
        startDateTextField.setText(collectible.getStartDate());
        finishDateTextField.setText(collectible.getFinishDate());
        descriptionTextArea.setText(collectible.getDescription());
        // todo pobranie statusu
        statusTextField.setText("do pobrania");
    }

    public void goToCollectible() {
        System.out.println("przejscie do obiektu");
    }
}
