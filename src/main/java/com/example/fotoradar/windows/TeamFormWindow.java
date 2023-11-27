package com.example.fotoradar.windows;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.components.TeamFormComponent;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;

public class TeamFormWindow implements Window {

    @FXML
    private TeamFormComponent teamForm;
    @FXML
    private Label windowLabel;
    @Setter
    private Stage dialogStage;
    @Setter
    private Collection parentCollection;

    @Setter
    private OnWindowClosedListener onWindowClosedListener;


    public void initialize() {
        setWindowLabel(parentCollection.getTitle());
    }

    private void setWindowLabel(String teamName) {
        windowLabel.setText(String.format("Dodaj zespol do kolekcji %s", teamName));
    }

    public void saveTeam(ActionEvent event) throws SQLException {
        System.out.println("zapisz zespol");

        System.out.println("TeamFormWindow.saveTeam: parentCollection: " + parentCollection);

        Team teamToAdd = new Team(
                teamForm.nameLabel.getText(),

                teamForm.descriptionTextArea.getText(),
                parentCollection.getId()
        );
        System.out.println("dane z formularza: " + teamToAdd);

        // dodanie obiektu do bazy
        TeamOperations teamOperations = new TeamOperations();
        teamOperations.addTeam(teamToAdd);

        // po wykonanej operacji zamknij okienko
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }
}