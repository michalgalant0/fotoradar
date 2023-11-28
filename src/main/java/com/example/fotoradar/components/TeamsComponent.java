package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.TeamsComponentFlag;
import com.example.fotoradar.models.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

public class TeamsComponent extends AnchorPane {
    @FXML
    private VBox teamsContainer;

    @Setter
    private ArrayList<Team> teams;

    private TeamsComponentFlag flag;

    public TeamsComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamsComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillTeamsVBox() throws IOException {
        teamsContainer.getChildren().clear();
        for (Team team : teams) {
            TeamComponent teamComponent = new TeamComponent();
            teamComponent.setTeam(team);
            teamComponent.fillComponent();
            teamsContainer.getChildren().add(teamComponent);
        }
    }
}
