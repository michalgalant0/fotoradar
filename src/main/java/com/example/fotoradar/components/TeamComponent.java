package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.ThumbnailOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import com.example.fotoradar.components.TeamRowComponent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamComponent extends AnchorPane{
    @FXML
    public Label nameLabel;
    @FXML
    public Label descriptionLabel;

    @FXML
    private VBox teamsContainer;
    @FXML
    private ScrollPane scrollPane;

    @Setter
    private Team team;

    private ArrayList<Team> teamy;


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

    public void setDescriptionLabel(String status) {
        descriptionLabel.setText(status);
    }

    public void fillTeamsVBox() throws IOException, SQLException {
       /* for (Team team : teamy) {
            teamRowComponent teamsRow = new CollectionRowComponent();
            teamsRow.setCollectible(team);
            teamsRow.setNameLabel(teams.getTitle());
            teamsRow.setDescription(team.getDescription());


            collectiblesContainer.getChildren().add(collectionRow);
        }*/
    }

}
