package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ParametersView {
    @FXML
    public CollectionFormComponent collectionFormComponent;
    @FXML
    public TeamsComponent teamsComponent;
    @FXML
    public Label windowLabel;

    @Setter
    private Collection collection;

    public void initialize() {
        System.out.println("ParametersView.initialize: "+collection);
        setWindowLabel(collection.getTitle());
        fillFormComponent();
    }

    public void setWindowLabel(String collectionName) {
        windowLabel.setText(String.format("kolekcje/ %s/ parametry", collectionName));
    }

    public void fillFormComponent() {
        collectionFormComponent.titleTextField.setText(collection.getTitle());
        collectionFormComponent.startDatePicker.setValue(LocalDate.parse(collection.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        collectionFormComponent.finishDatePicker.setValue(LocalDate.parse(collection.getFinishDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        collectionFormComponent.descriptionTextArea.setText(collection.getDescription());
    }

    @FXML
    private void saveCollection() throws SQLException {
        System.out.println("zapisz kolekcje");

        collection.setTitle(collectionFormComponent.titleTextField.getText());
        collection.setStartDate(collectionFormComponent.startDatePicker.getValue().toString());
        collection.setFinishDate(collectionFormComponent.finishDatePicker.getValue().toString());
        collection.setDescription(collectionFormComponent.descriptionTextArea.getText());

        // update kolekcji do bazy
        CollectionOperations collectionOperations = new CollectionOperations();
        collectionOperations.updateCollection(collection);

        System.out.println(collection);
    }
    @FXML
    private void manageTeams(ActionEvent event) throws IOException {
        System.out.println("zarządzaj zespołami");
        new SwitchScene().switchScene(event, "teamsView");
    }
    @FXML
    private void backToCollection(ActionEvent event) throws IOException {
        System.out.println("powrót do kolekcji");

        // utworzenie kontrolera widoku kolekcji
        CollectionView collectionView = new CollectionView();
        collectionView.setCollection(collection);

        new SwitchScene().switchScene(event, "collectionView", collectionView);
    }
}
