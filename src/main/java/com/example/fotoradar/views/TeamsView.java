package com.example.fotoradar.views;

import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Button backToButton;

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
}
