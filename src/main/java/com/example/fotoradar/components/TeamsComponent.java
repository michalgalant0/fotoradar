package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
import com.example.fotoradar.views.CollectionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import java.io.IOException;

public class TeamsComponent extends AnchorPane {
    @FXML
    private Label headerLabel;
    @FXML
    private VBox teamsContainer;
    @FXML
    private ScrollPane scrollPane;

    @Setter
    private Collection collection;
    private ArrayList<Team> teams;

    public TeamsComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamsComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }
    public void setHeaderLabel(String name) {
        headerLabel.setText(name);
    }

    public void setTeams(ArrayList<Team> teamy) {
        this.teams = teamy;
    }

    public void fillTeamVBox() throws IOException, SQLException {
        System.out.println("wyswietlanie teamow");
        for (Team team : teams) {
            TeamRowComponent teamsRow = new TeamRowComponent();
            System.out.println("TeamsComponent.fillTeamsVBox: "+team);
            teamsRow.setTeam(team);
            teamsRow.setNameLabel(team.getName());
            teamsRow.setDescriptionTextArea(team.getDescription());


            teamsContainer.getChildren().add(teamsRow);
        }
    }

}
