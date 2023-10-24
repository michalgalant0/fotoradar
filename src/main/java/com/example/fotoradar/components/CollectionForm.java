package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter @Setter
public class CollectionForm extends AnchorPane {
    @FXML
    public TextField titleTextField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker finishDatePicker;
    @FXML
    public TextArea descriptionTextArea;

    public CollectionForm() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectionForm.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
