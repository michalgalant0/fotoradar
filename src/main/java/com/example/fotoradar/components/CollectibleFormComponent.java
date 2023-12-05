package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.databaseOperations.StatusManager;
import com.example.fotoradar.models.Status;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

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
    public ComboBox<Status> statusComboBox;

    private ArrayList<Status> statuses;

    public CollectibleFormComponent() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/CollectibleFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }}

    public void initialize() {
        fillStatusComboBox();
    }

    private void fillStatusComboBox() {
        statuses = StatusManager.getInstance().getStatuses();
        statusComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Status status) {
                return status != null ? status.getName() : "";
            }
            @Override
            public Status fromString(String string) {
                return findStatusByName(string);
            }
        });

        statusComboBox.setItems(FXCollections.observableArrayList(statuses));
    }

    private Status findStatusByName(String name) {
        for (Status status : statuses) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        return null; // Możesz obsłużyć przypadek, gdy wersja nie zostanie znaleziona
    }

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
    public void setStatusComboBox(Status status) {
        statusComboBox.setValue(status);
    }
}
