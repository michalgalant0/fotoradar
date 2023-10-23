package com.example.fotoradar.components;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class VersionForm extends AnchorPane {
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

    public VersionForm() {}

    public void initialize() {}

    @FXML
    public void editTeam() {
        System.out.println("edycja zespołu");
    }

    @FXML
    public void addTeam() {
        System.out.println("dodanie zespołu");
    }
}
