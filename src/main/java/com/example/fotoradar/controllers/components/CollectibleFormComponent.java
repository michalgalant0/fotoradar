package com.example.fotoradar.controllers.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;

public class CollectibleFormComponent extends AnchorPane {
    @FXML
    public TextField titleTextField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker finishDatePicker;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public ComboBox statusComboBox;

    public CollectibleFormComponent() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectibleFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }}

    public void initialize() {}

    public void setTitleTextField(String collectibleTitle) {
        titleTextField.setText(collectibleTitle);
    }
    public void setStartDatePicker(String startDate) {
        // todo String to Date
        startDate = "2019-02-04"; //todo test
        startDatePicker.setValue(LocalDate.parse(startDate));
    }
    public void setFinishDatePicker(String finishDate) {
        // todo String to Date
        finishDate = "2018-05-05"; //todo test
        finishDatePicker.setValue(LocalDate.parse(finishDate));
    }
    public void setDescriptionTextArea(String collectibleDescription) {
        descriptionTextArea.setText(collectibleDescription);
    }
    public void setStatusComboBox(String value) {
        // todo poprawic
        statusComboBox.setValue(value);
    }
}
