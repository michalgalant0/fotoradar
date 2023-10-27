package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.views.CollectibleView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;

public class CollectibleComponent extends AnchorPane {

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

    @Setter
    private Collectible collectible;

    public CollectibleComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectibleComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    private void setHeaderLabel(String name) {
        headerLabel.setText(name);
    }

    public void fillComponent() {
        System.out.println("CollectibleComponent.fillComponent: "+collectible);
        setHeaderLabel(collectible.getTitle());
        startDateTextField.setText(collectible.getStartDate());
        finishDateTextField.setText(collectible.getFinishDate());
        descriptionTextArea.setText(collectible.getDescription());
        // todo pobranie statusu z bazy
        statusTextField.setText("do pobrania");
    }

    @FXML
    public void goToCollectible(ActionEvent event) throws IOException, SQLException {
        System.out.println("przejscie do obiektu");
        System.out.println("CollectibleComponent.goToCollectible: "+collectible);
        // utworzenie kontrolera widoku docelowego
        CollectibleView collectibleView = new CollectibleView();
        collectibleView.setCollectible(collectible);
        // załadowanie nowej sceny z przekazanym kontrolerem
        new SwitchScene().switchScene(event, "collectibleView", collectibleView);
    }
}
