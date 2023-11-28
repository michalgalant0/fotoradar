package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.components.TeamFormComponent;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamsView {
    @FXML
    private Label windowLabel;
    @FXML
    private TeamsComponent teamsComponent;
    @FXML
    private TeamFormComponent teamFormComponent;

    private TeamOperations teamOperations;

    @Setter
    private Collection parentCollection;

    private ArrayList<Team> teams;

    public TeamsView() {
        try {
            teamOperations = new TeamOperations();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() throws SQLException, IOException {
        System.out.println("TeamsView.initialize: parentCollection "+parentCollection);
        teams = teamOperations.getAllCollectionTeams(parentCollection.getId());
        System.out.println("TeamsView.initialize: teams "+teams);
        setWindowLabel(parentCollection.getTitle());
        // wypełnienie bloku zespołów danymi z bazy
        teamsComponent.setTeams(teams);
        teamsComponent.fillTeamsVBox();
    }

    private void setWindowLabel(String collectionName) {
        windowLabel.setText("kolekcje/ "+collectionName + "/ zespoły");
    }

    @FXML
    private void backToParameters(ActionEvent event) throws IOException {
        ParametersView parametersView = new ParametersView();
        parametersView.setCollection(parentCollection);
        new SwitchScene().switchScene(event, "parametersView", parametersView);
    }

    @FXML
    private void addTeam(ActionEvent event) throws SQLException {
        System.out.println("zapisz zespol");

        Team teamToAdd = new Team(
                teamFormComponent.nameTextField.getText(),
                teamFormComponent.descriptionTextArea.getText(),
                parentCollection.getId()
        );
        System.out.println("dane z formularza: " + teamToAdd);

        new TeamOperations().addTeam(teamToAdd);

        refresh();
    }

    private void refresh() {
        try {
            teams = teamOperations.getAllCollectionTeams(parentCollection.getId());
            // wypełnienie bloku zespołów danymi z bazy
            teamsComponent.setTeams(teams);
            teamsComponent.fillTeamsVBox();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
