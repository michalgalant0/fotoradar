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

    @FXML
    private void saveTeam(ActionEvent event) throws SQLException {
        System.out.println("zapisz zespol");

        Team teamToAdd = new Team(
                teamForm.nameTextField.getText(),
                teamForm.descriptionTextArea.getText(),
                parentCollectionId
        );
        System.out.println("dane z formularza: " + teamToAdd);

        new TeamOperations().addTeam(teamToAdd);

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
