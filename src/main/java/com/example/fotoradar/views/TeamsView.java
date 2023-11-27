package com.example.fotoradar.views;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.CollectionFormComponent;
import com.example.fotoradar.components.TeamFormComponent;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
public class TeamsView {

    @FXML
    public TeamFormComponent teamFormComponent;
    @FXML
    public TeamsComponent teamComponent;
    @FXML
    public Label windowLabel;

    @FXML
    public Label nameLabel;
    @FXML
    public TextArea descriptionTextArea;

    @Setter
    private Team teams;


    @Setter
    private Collection collection;
    private String collectionPath = "%s/KOLEKCJE/%s";
    private String teamsPath = "%s/KOLEKCJE/%s/PATEMETRY/%s/Z";

    public void initialize() {
        System.out.println("ParametersView.initialize: "+teams);
        setWindowLabel(teams.getName());
        fillTeamComponent();
        collectionPath = String.format(collectionPath, System.getProperty("user.dir"), collection.getTitle());
    }
    public void setWindowLabel(String collectionName) {
        windowLabel.setText(String.format("kolekcje/ %s/ parametry", collectionName));
    }
    public void fillTeamComponent() {
        System.out.println("teamComponent.fillteam: "+teams);
        setWindowLabel(teams.getName());
        descriptionTextArea.setText(teams.getDescription());
        // todo pobranie statusu z bazy

    }

    @FXML
    private void backToCollection(ActionEvent event) throws IOException {
        System.out.println("powrót do kolekcji");

        // utworzenie kontrolera widoku kolekcji
        CollectionView collectionView = new CollectionView();
        collectionView.setCollection(collection);

        new SwitchScene().switchScene(event, "collectionView", collectionView);
    }

    private void refresh() throws SQLException, IOException {
        setWindowLabel(teams.getName());
        fillTeamComponent();
        teamsPath = String.format(teamsPath, System.getProperty("user.dir"), teams.getName());
    }


    /*private void refresh() throws SQLException, IOException {
        // ustawienie nagłówka okna
        setWindowLabel(team.getName());
        // wypełnienie listy obiektów danymi pobranymi z bazy
        teams = teamOperations.getAllTeams(team.getId());
        teamsComponent.setTeam(team);
        teamsComponent.setTeamName(team.getName());
        teamsComponent.fillTeamVBox();
    }*/
}
