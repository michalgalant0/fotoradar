package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Team;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import com.example.fotoradar.models.Collection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamsComponent extends AnchorPane {
    @FXML
    private VBox teamsContainer;

    @Setter
    private ArrayList<Team> teams;

    public TeamsComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamsComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillTeamsHBox() throws IOException {
        try {
            TeamOperations teamOperations = new TeamOperations();
            List<Team> teamsList = teamOperations.getAllTeams();

            for (Team team : teamsList) {
                // utworzenie komponentu zespołu
                TeamComponent teamComponent = new TeamComponent();
                // przekazanie zespołu do komponentu
                teamComponent.setTeam(team);

                // wypełnienie komponentu danymi zespołu
                teamComponent.initialize();
                teamsContainer.getChildren().add(teamComponent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
