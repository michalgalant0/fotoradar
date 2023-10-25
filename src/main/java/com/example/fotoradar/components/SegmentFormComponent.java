package com.example.fotoradar.components;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SegmentFormComponent extends AnchorPane {
    @FXML
    public TextField numberTextField;
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
    public ComboBox versionComboBox;
    @FXML
    public ComboBox statusComboBox;

    public SegmentFormComponent() {}

    public void initialize() {}

    @FXML
    public void openVersion() {
        System.out.println("otwarcie wersji");
    }

    @FXML
    public void addVersion() {
        System.out.println("dodanie wersji");
    }
}
