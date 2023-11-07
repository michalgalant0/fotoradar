package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Version;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;

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
    public ComboBox teamComboBox;

    public VersionFormComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/VersionFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {}

    public void fillForm(Version version) {
        nameTextField.setText(version.getName());
        startDatePicker.setValue(LocalDate.parse(version.getStartDate()));
        finishDatePicker.setValue(LocalDate.parse(version.getFinishDate()));
        descriptionTextArea.setText(version.getDescription());
        // todo dodać wypełnianie listy zespołami pod kolekcją do której wersja należy
    }

    @FXML
    public void editTeam() {
        System.out.println("edycja zespołu");
    }

    @FXML
    public void addTeam() {
        System.out.println("dodanie zespołu");
    }
}
