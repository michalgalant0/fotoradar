package com.example.fotoradar.windows;

import com.example.fotoradar.components.TeamFormComponent;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;

public class TeamFormWindow implements Window {
    @FXML
    private TeamFormComponent teamForm;
    @Setter
    private Stage dialogStage;

    @Setter
    private OnWindowClosedListener onWindowClosedListener;

    @Setter
    private int parentCollectionId;

    @Setter
    private Team team;

    public TeamFormWindow() {
    }

    public void initialize() {
        if (team != null) {
            teamForm.teamFormLabel.setText("edytuj zespół "+team.getName());
            teamForm.setTeam(team);
            teamForm.fillForm();
        }
        else {
            teamForm.teamFormLabel.setText("dodaj nowy zespół");
        }
    }

    @FXML
    private void saveTeam(ActionEvent event) throws SQLException {
        if (team != null) {
            System.out.println("aktualizacja isntniejącego zespolu");

            team.setName(teamForm.nameTextField.getText());
            team.setDescription(teamForm.descriptionTextArea.getText());
            System.out.println("dane z formularza: " + team);

            new TeamOperations().updateTeam(team);
        } else {
            System.out.println("dodanie nowego zespolu");

            Team teamToAdd = new Team(
                    teamForm.nameTextField.getText(),
                    teamForm.descriptionTextArea.getText(),
                    parentCollectionId
            );
            System.out.println("dane z formularza: " + teamToAdd);

            new TeamOperations().addTeam(teamToAdd);
        }

        // zamkniecie okienka po wykonanej operacji
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }

    @FXML
    private void cancel(ActionEvent event) {
        System.out.println("anuluj");
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }
}
