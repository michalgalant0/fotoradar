package com.example.fotoradar.views;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.summaryGenerator.SummaryGenerator;
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

    private String collectionPath = "%s/KOLEKCJE/%s";

    public void initialize() {
        System.out.println("ParametersView.initialize: "+collection);
        setWindowLabel(collection.getTitle());
        fillFormComponent();
        collectionPath = String.format(collectionPath, System.getProperty("user.dir"), collection.getTitle());
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
        String oldPath = collectionPath;

        collection.setTitle(collectionFormComponent.titleTextField.getText());
        collection.setStartDate(collectionFormComponent.startDatePicker.getValue().toString());
        collection.setFinishDate(collectionFormComponent.finishDatePicker.getValue().toString());
        collection.setDescription(collectionFormComponent.descriptionTextArea.getText());

        // update kolekcji do bazy
        CollectionOperations collectionOperations = new CollectionOperations();
        collectionOperations.updateCollection(collection);

        // aktualizacja nazwy katalogu
        String newName = collection.getTitle();
        DirectoryOperator.getInstance().updateDirectoryName(oldPath, newName);

        System.out.println(collection);

        //odswiezenie okna po zapisie
        setCollection(collection);
        refresh();
    }
    @FXML
    private void manageTeams(ActionEvent event) throws IOException {
        System.out.println("zarządzaj zespołami");
        new SwitchScene().switchScene(event, "teamsView");
    }
    @FXML
    private void createReport(ActionEvent event) {
        System.out.println("generowanie raportu");
        try {
            new SummaryGenerator();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void backToCollection(ActionEvent event) throws IOException {
        System.out.println("powrót do kolekcji");

        // utworzenie kontrolera widoku kolekcji
        CollectionView collectionView = new CollectionView();
        collectionView.setCollection(collection);

        new SwitchScene().switchScene(event, "collectionView", collectionView);
    }

    private void refresh() {
        setWindowLabel(collection.getTitle());
        fillFormComponent();
        collectionPath = String.format(collectionPath, System.getProperty("user.dir"), collection.getTitle());
    }
}
