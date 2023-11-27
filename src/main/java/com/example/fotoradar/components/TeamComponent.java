package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
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

public class TeamComponent extends AnchorPane{

    @FXML
    private VBox teamsContainer;

    @FXML
    public Label nameLabel;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    private VBox teamContainer;

    @Setter
    private Team team;


    private ArrayList<Team> teams;
    @Setter
    private Collectible collectible;
    @Setter
    private Collection collection;

    public void initialize() {
    }

    public TeamComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }

    public void setDescriptionTextArea(String description) {
        descriptionTextArea.setText(description);
    }

    public void fillTeamVBox() throws IOException, SQLException {
        System.out.println("wyswietlanie teamow");
        for (Team team : teams) {
            TeamRowComponent teamsRow = new TeamRowComponent();
            teamsRow.setTeam(team);
            teamsRow.setNameLabel(team.getName());
            teamsRow.setDescriptionTextArea(team.getDescription());


            teamsContainer.getChildren().add(teamsRow);
        }

    }
    public void fillComponent() {
        System.out.println("teamComponent.fillteam: "+team);
        setNameLabel(team.getName());
        descriptionTextArea.setText(team.getDescription());
        // todo pobranie statusu z bazy

    }




}

