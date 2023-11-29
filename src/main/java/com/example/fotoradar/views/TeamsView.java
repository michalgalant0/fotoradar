package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.TeamsComponentFlag;
import com.example.fotoradar.components.TeamComponentLeftClickListener;
import com.example.fotoradar.components.TeamFormComponent;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamsView implements TeamComponentLeftClickListener {
    @FXML
    private Label windowLabel;
    @FXML
    private TeamsComponent teamsComponent;
    @FXML
    private TeamFormComponent teamFormComponent;
    @FXML
    private Button submitFormButton;

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
        teamFormComponent.setLeftClickListener(this);
        teamsComponent.setTeams(teams);
        teamsComponent.setFlag(TeamsComponentFlag.TEAMS_VIEW);
        teamsComponent.setParentView(this);
        teamsComponent.fillTeamsComponent();
        setSubmitButtonAction(Mode.SAVE);
    }

    public void setSubmitButtonAction(Mode mode) {
        if (mode == Mode.SAVE)
            submitFormButton.setOnAction(event -> {
                System.out.println("zapisz zespol");
                Team teamToAdd = new Team(
                        teamFormComponent.nameTextField.getText(),
                        teamFormComponent.descriptionTextArea.getText(),
                        parentCollection.getId()
                );
                System.out.println("dane z formularza: " + teamToAdd);
                try {
                    new TeamOperations().addTeam(teamToAdd);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                refresh();
            });
        else if (mode == Mode.UPDATE) {
            submitFormButton.setOnAction(event -> {
                System.out.println("aktualizuj zespol o id: "+teamFormComponent.getTeam().getId());
                Team teamToUpdate = teamFormComponent.getTeam();
                teamToUpdate.setName(teamFormComponent.nameTextField.getText());
                teamToUpdate.setDescription(teamFormComponent.descriptionTextArea.getText());

                try {
                    new TeamOperations().updateTeam(teamToUpdate);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                refresh();
            });
        }
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

    private void refresh() {
        try {
            teams = teamOperations.getAllCollectionTeams(parentCollection.getId());
            // wypełnienie bloku zespołów danymi z bazy
            teamsComponent.setTeams(teams);
            teamsComponent.fillTeamsComponent();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTeamComponentClicked(Team team) {
        System.out.println("TeamsView.onTeamComponentClicked: "+team);
        teamFormComponent.teamFormLabel.setText("Edytuj zespół");
        teamFormComponent.setTeam(team);
        teamFormComponent.fillForm();
        teamFormComponent.clearFormButton.setVisible(true);
        setSubmitButtonAction(Mode.UPDATE);
    }

    @Override
    public void onFormCleared() {
        setSubmitButtonAction(Mode.SAVE);
    }

    enum Mode {
        SAVE, UPDATE
    }
}
