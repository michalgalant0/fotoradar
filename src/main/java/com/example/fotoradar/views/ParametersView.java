package com.example.fotoradar.views;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.TeamsComponentFlag;
import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.components.IndicatorsComponent;
import com.example.fotoradar.components.TeamComponentRightClickListener;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
import com.example.fotoradar.summaryGenerator.SummaryGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ParametersView implements TeamComponentRightClickListener {
    @FXML
    public CollectionFormComponent collectionFormComponent;
    @FXML
    public TeamsComponent teamsComponent;
    @FXML
    public Label windowLabel;
    @FXML
    public IndicatorsComponent indicatorsComponent;

    @Setter
    private Collection collection;

    private ArrayList<Team> teams;

    private String collectionPath = Paths.get("%s","KOLEKCJE","%s").toString();

    public void initialize() {
        System.out.println("ParametersView.initialize: "+collection);
        setWindowLabel(collection.getTitle());
        fillFormComponent();
        fillTeamsComponent();
        collectionPath = String.format(collectionPath, Main.getDefPath(), collection.getTitle());

        double[] indicatorData;
        try {
            indicatorData = new CollectionOperations().fetchProgressAndSizeInfo(collection.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        fillIndicator(indicatorData[0], indicatorData[1]);
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

    public void fillTeamsComponent() {
        try {
            teamsComponent.setTeams(
                    new TeamOperations().getAllCollectionTeams(collection.getId())
            );
            teamsComponent.setParentView(this);
            teamsComponent.setFlag(TeamsComponentFlag.PARAMETERS_VIEW);
            AnchorPane.setBottomAnchor(teamsComponent.scrollPane, 32.0);
            AnchorPane.setTopAnchor(teamsComponent.scrollPane, 32.0);
            teamsComponent.fillTeamsComponent();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
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

        TeamsView teamsView = new TeamsView();
        teamsView.setParentView(this);
        teamsView.setParentCollection(collection);

        new SwitchScene().switchScene(event, "teamsView", teamsView);
    }
    @FXML
    private void createReport(ActionEvent event) {
        System.out.println("generowanie raportu");
        try {
            new SummaryGenerator(collection);
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
        fillTeamsComponent();
        collectionPath = String.format(collectionPath, Main.getDefPath(), collection.getTitle());
    }

    @Override
    public void onDeletePerformed() {
        refresh();
    }

    public void fillIndicator(double progressValue, double sizeValue) {
        indicatorsComponent.setProgress(progressValue);
        indicatorsComponent.setSize(sizeValue);
        indicatorsComponent.addWaveAnimation();
    }
}
