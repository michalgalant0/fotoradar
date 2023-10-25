package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    @Setter
    private Collection collection;

    public void initialize() throws SQLException {
        // tutaj pobranie z bazy testowo, normalnie przekazanie z poprzedniego modułu
        setCollection(new CollectionOperations().getCollectionById(8));

        collectionFormComponent.titleTextField.setText(collection.getTitle());
        collectionFormComponent.startDatePicker.setValue(LocalDate.parse(collection.getStartDate(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        collectionFormComponent.finishDatePicker.setValue(LocalDate.parse(collection.getFinishDate(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        collectionFormComponent.descriptionTextArea.setText(collection.getDescription());
    }

    @FXML
    private void saveCollection() {
        System.out.println("zapisz kolekcje");
        collection.setTitle(collectionFormComponent.titleTextField.getText());
        collection.setStartDate(collectionFormComponent.startDatePicker.getValue().toString());
        collection.setFinishDate(collectionFormComponent.finishDatePicker.getValue().toString());
        collection.setDescription(collectionFormComponent.descriptionTextArea.getText());
        System.out.println(collection);
    }
    @FXML
    private void manageTeams(ActionEvent event) {
        System.out.println("zarządzaj zespołami");
    }
    @FXML
    private void backToCollection(ActionEvent event) throws IOException {
        System.out.println("powrót do kolekcji");
        new SwitchScene(event, "collectionView");
    }
}
