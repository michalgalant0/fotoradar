package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

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

    public VersionForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/VersionForm.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

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
