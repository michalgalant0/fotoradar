package com.example.fotoradar.views;

import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.TeamsComponentFlag;
import com.example.fotoradar.components.TeamComponentLeftClickListener;
import com.example.fotoradar.components.TeamComponentRightClickListener;
import com.example.fotoradar.components.TeamFormComponent;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamsView implements TeamComponentLeftClickListener, TeamComponentRightClickListener {
    @FXML
    private Label windowLabel;
    @FXML
    private TeamsComponent teamsComponent;
    @FXML
    private TeamFormComponent teamFormComponent;
    @FXML
    private Button submitFormButton;
    @FXML
    private Button backToParentButton;

    private TeamOperations teamOperations;

    @Setter
    private Collection parentCollection;
    @Setter
    private Object parentView;

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

        if (parentView instanceof ParametersView)
            backToParentButton.setText("POWRÓT DO PARAMETRÓW");
        else if (parentView instanceof VersionView)
            backToParentButton.setText("POWRÓT DO WERSJI");
    }

    public void setSubmitButtonAction(Mode mode) {
        submitFormButton.setOnAction(event -> {
            if (mode == Mode.SAVE) {
                System.out.println("zapisz zespół");
                String name = teamFormComponent.nameTextField.getText();
                String description = teamFormComponent.descriptionTextArea.getText();

                // Przykład sprawdzenia i wyświetlenia komunikatu w miejscu pola tytułu
                if (name.isEmpty()) {
                    // Pobranie wcześniej utworzonego pola tekstowego do wprowadzania tytułu
                    TextField titleTextField = teamFormComponent.nameTextField;

                    // Ustawienie czerwonej ramki lub tła dla pola tytułu jako wskazanie błędu
                    titleTextField.setStyle("-fx-border-color: red;"); // Możesz dostosować to według potrzeb

                    // Wstawienie komunikatu w miejscu pola tytułu
                    titleTextField.setPromptText("Pole nazwy nie może być puste!");
                    return;
                }
                else{
                    // Jeśli tytuł nie jest pusty, przywróć domyślny styl pola tekstowego
                    TextField titleTextField = teamFormComponent.nameTextField;
                    titleTextField.setStyle(""); // Usunięcie dodanego stylu (reset do domyślnego)

                    // Możesz także usunąć komunikat, jeśli taki został wyświetlony
                    titleTextField.setPromptText(""); // Usunięcie wyświetlonego komunikatu
                }

                // Reszta kodu z zapisem obiektu Team
                String descriptionValue = !description.isEmpty() ? description : null;
                Team teamToAdd = new Team(name, descriptionValue, parentCollection.getId());
                System.out.println("dane z formularza: " + teamToAdd);
                try {
                    new TeamOperations().addTeam(teamToAdd);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                refresh();
            } else if (mode == Mode.UPDATE) {
                System.out.println("aktualizuj zespół o id: " + teamFormComponent.getTeam().getId());
                Team teamToUpdate = teamFormComponent.getTeam();
                String name = teamFormComponent.nameTextField.getText();

                // Przykład sprawdzenia i wyświetlenia komunikatu w miejscu pola tytułu
                if (name.isEmpty()) {
                    // Pobranie wcześniej utworzonego pola tekstowego do wprowadzania tytułu
                    TextField titleTextField = teamFormComponent.nameTextField;

                    // Ustawienie czerwonej ramki lub tła dla pola tytułu jako wskazanie błędu
                    titleTextField.setStyle("-fx-border-color: red;"); // Możesz dostosować to według potrzeb

                    // Wstawienie komunikatu w miejscu pola tytułu
                    titleTextField.setPromptText("Pole nazwy nie może być puste!");
                    return;
                }
                else{
                    // Jeśli tytuł nie jest pusty, przywróć domyślny styl pola tekstowego
                    TextField titleTextField = teamFormComponent.nameTextField;
                    titleTextField.setStyle(""); // Usunięcie dodanego stylu (reset do domyślnego)

                    // Możesz także usunąć komunikat, jeśli taki został wyświetlony
                    titleTextField.setPromptText(""); // Usunięcie wyświetlonego komunikatu
                }

                String description = teamFormComponent.descriptionTextArea.getText();
                String descriptionValue = description != null && !description.isEmpty() ? description : null;
                teamToUpdate.setName(name);
                teamToUpdate.setDescription(descriptionValue);

                try {
                    new TeamOperations().updateTeam(teamToUpdate);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                refresh();
            }
        });
    }



    private void setWindowLabel(String collectionName) {
        windowLabel.setText("kolekcje/ "+collectionName + "/ zespoły");
    }

    @FXML
    private void backToParent(ActionEvent event) throws IOException {
        if (parentView instanceof ParametersView parametersView)
            new SwitchScene().switchScene(event, "parametersView", parametersView);
        else if (parentView instanceof VersionView versionView)
            new SwitchScene().switchScene(event, "versionView", versionView);
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

    @Override
    public void onDeletePerformed() {
        refresh();
    }

    enum Mode {
        SAVE, UPDATE
    }
}
