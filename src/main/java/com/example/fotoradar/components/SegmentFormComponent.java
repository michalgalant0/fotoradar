package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

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

    public SegmentFormComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/SegmentFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {}

    @FXML
    public void openVersion(ActionEvent event) throws IOException {
        System.out.println("otwarcie wersji");
        new SwitchScene().switchScene(event, "versionView");
    }

    @FXML
    public void addVersion(ActionEvent event) throws IOException {
        System.out.println("dodanie wersji");
        new SwitchScene().switchScene(event, "versionView");
    }
}
