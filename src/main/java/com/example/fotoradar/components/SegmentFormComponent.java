package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.models.Segment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;

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

    public void fillForm(Segment segment) {
        // testowo
        numberTextField.setText(String.valueOf(segment.getId()));
        String title = segment.getTitle() == null ? "wprowadź nazwę segmentu" : segment.getTitle();
        nameTextField.setText(title);
        String startDate = segment.getStartDate() == null ? "1900-01-01" : segment.getStartDate();
        startDatePicker.setValue(LocalDate.parse(startDate));
        String finishDate = segment.getStartDate() == null ? "1900-01-01" : segment.getStartDate();
        finishDatePicker.setValue(LocalDate.parse(finishDate));
        String description = segment.getDescription() == null ? "wprowadź opis segmentu" : segment.getDescription();
        descriptionTextArea.setText(description);
        versionComboBox.setValue("wersje do pobrania");
        statusComboBox.setValue("status do pobrania");
    }
}
