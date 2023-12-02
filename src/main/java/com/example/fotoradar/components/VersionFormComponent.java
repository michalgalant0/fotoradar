package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Team;
import com.example.fotoradar.models.Version;
import com.example.fotoradar.views.VersionView;
import com.example.fotoradar.windows.TeamFormWindow;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class VersionFormComponent extends AnchorPane {
    @FXML
    public TextField nameTextField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public TextField startTimeTextField;
    @FXML
    public DatePicker finishDatePicker;
    @FXML
    public TextField finishTimeTextField;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public ComboBox<Team> teamComboBox;

    private ArrayList<Team> teams;
    @Setter
    private int parentCollectionId;

    @Setter
    private VersionView versionView;

    public VersionFormComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/VersionFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
        System.out.println("\n\nVersionFormComponent.initialize:");
        System.out.println("parentCollectionId "+parentCollectionId);
        System.out.println(teams);
    }

    public void fillForm(Version version) {
        nameTextField.setText(version.getName());
        startDatePicker.setValue(LocalDate.parse(version.getStartDate()));
        finishDatePicker.setValue(LocalDate.parse(version.getFinishDate()));
        descriptionTextArea.setText(version.getDescription());
    }

    public void fillTeamComboBox() throws SQLException {
        System.out.println("VersionFormComponent.fillTeamComboBox parentCollectionId: "+parentCollectionId);
        teams = new TeamOperations().getAllCollectionTeams(parentCollectionId);
        System.out.println("VersionFormComponent.fillTeamComboBox teams: "+teams);
        teamComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Team team) {
                return team != null ? team.getName() : "";
            }

            @Override
            public Team fromString(String s) {
                return findTeamByName(s);
            }
        });

        teamComboBox.setItems(FXCollections.observableArrayList(teams));
    }

    private Team findTeamByName(String s) {
        for (Team team : teams) {
            if (team.getName().equals(s))
                return team;
        }
        return null;
    }

    @FXML
    public void editTeam(ActionEvent event) throws IOException {
        System.out.println("edycja zespołu: "+teamComboBox.getValue());

        TeamFormWindow teamFormWindow = new TeamFormWindow();
        teamFormWindow.setTeam(teamComboBox.getValue());
        teamFormWindow.setParentCollectionId(parentCollectionId);
        teamFormWindow.setOnWindowClosedListener(versionView);
        new SwitchScene().displayWindow("TeamFormWindow", "edytuj zespół", teamFormWindow);
    }

    @FXML
    public void addTeam(ActionEvent event) throws IOException {
        System.out.println("dodanie zespołu");
        TeamFormWindow teamFormWindow = new TeamFormWindow();
        teamFormWindow.setParentCollectionId(parentCollectionId);
        teamFormWindow.setOnWindowClosedListener(versionView);
        new SwitchScene().displayWindow("TeamFormWindow", "dodaj zespół", teamFormWindow);
    }
}
