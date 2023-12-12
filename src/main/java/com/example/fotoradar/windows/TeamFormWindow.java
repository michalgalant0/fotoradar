package com.example.fotoradar.windows;

import com.example.fotoradar.components.TeamFormComponent;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
        String name = teamForm.nameTextField.getText();
        // Przykład sprawdzenia i wyświetlenia komunikatu w miejscu pola tytułu
        if (name.isEmpty()) {
            // Pobranie wcześniej utworzonego pola tekstowego do wprowadzania tytułu
            TextField titleTextField = teamForm.nameTextField;

            // Ustawienie czerwonej ramki lub tła dla pola tytułu jako wskazanie błędu
            titleTextField.setStyle("-fx-border-color: red;"); // Możesz dostosować to według potrzeb

            // Wstawienie komunikatu w miejscu pola tytułu
            titleTextField.setPromptText("Pole nazwy nie może być puste!");
            return;
        }

        String description = teamForm.descriptionTextArea.getText();

        if (team != null) {
            System.out.println("aktualizacja istniejącego zespołu");

            team.setName(name);
            team.setDescription(description != null && !description.isEmpty() ? description : null);
            System.out.println("dane z formularza: " + team);

            new TeamOperations().updateTeam(team);
        } else {
            System.out.println("dodanie nowego zespołu");

            Team teamToAdd = new Team(name, description.isEmpty() ? null : description, parentCollectionId);
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
