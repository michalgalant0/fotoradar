package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Team;
import com.example.fotoradar.models.Version;
import com.example.fotoradar.windows.OnWindowClosedListener;
import com.example.fotoradar.windows.TeamFormWindow;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private OnWindowClosedListener onWindowClosedListener;

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

        // Ustawienie wartości dla startDatePicker
        String startDate = version.getStartDate();
        if (startDate != null && !startDate.isEmpty()) {
            startDatePicker.setValue(LocalDate.parse(startDate.substring(0, 10)));
        } else {
            startDatePicker.setValue(null);
        }
        startTimeTextField.setText(extractTimeFromDate(version.getStartDate())); // Extracting time part

        // Ustawienie wartości dla finishDatePicker
        String finishDate = version.getFinishDate();
        if (finishDate != null && !finishDate.isEmpty()) {
            finishDatePicker.setValue(LocalDate.parse(finishDate.substring(0, 10)));
        } else {
            finishDatePicker.setValue(null);
        }
        finishTimeTextField.setText(extractTimeFromDate(version.getFinishDate())); // Extracting time part

        // Ustawienie wartości dla descriptionTextArea
        String description = version.getDescription();
        if (description != null && !description.isEmpty()) {
            descriptionTextArea.setText(description);
        } else {
            descriptionTextArea.setText(null);
        }
    }

    private String extractTimeFromDate(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty()) {
            try {
                LocalDateTime dateTimeValue = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                return dateTimeValue.format(DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                // Obsługa przypadku, gdy nie można sparsować czasu (brak godziny)
                LocalDate dateValue = LocalDate.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return ""; // Zwrócenie pustego ciągu znaków dla samej daty
            }
        }
        return null; // Zwrócenie wartości null, jeśli przekazany ciąg znaków jest pusty
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
        teamFormWindow.setOnWindowClosedListener(onWindowClosedListener);
        new SwitchScene().displayWindow("TeamFormWindow", "edytuj zespół", teamFormWindow);
    }

    @FXML
    public void addTeam(ActionEvent event) throws IOException {
        System.out.println("dodanie zespołu");
        TeamFormWindow teamFormWindow = new TeamFormWindow();
        teamFormWindow.setParentCollectionId(parentCollectionId);
        teamFormWindow.setOnWindowClosedListener(onWindowClosedListener);
        new SwitchScene().displayWindow("TeamFormWindow", "dodaj zespół", teamFormWindow);
    }
}
