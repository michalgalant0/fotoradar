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

public class CollectibleForm extends AnchorPane {
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

    public CollectibleForm() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectibleForm.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }}

    public void initialize() {}
}
