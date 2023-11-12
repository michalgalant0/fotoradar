package com.example.fotoradar.controllers.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.VersionOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Version;
import com.example.fotoradar.controllers.views.VersionView;
import com.example.fotoradar.controllers.windows.VersionFormWindow;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

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
    public ComboBox<Version> versionComboBox;
    @FXML
    public ComboBox statusComboBox;

    private Segment segment;
    @Setter
    private String parentCollectionName;
    @Setter
    private Collectible parentCollectible;

    private ArrayList<Version> versions;

    public SegmentFormComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/SegmentFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void initialize() {
    }

    public void fillVersionComboBox() throws SQLException {
        versions = new VersionOperations().getAllVersions(segment.getId());

        versionComboBox.setConverter(new StringConverter<Version>() {
            @Override
            public String toString(Version version) {
                return version != null ? version.getName() : "";
            }

            @Override
            public Version fromString(String string) {
                return findVersionByName(string);
            }
        });

        versionComboBox.setItems(FXCollections.observableArrayList(versions));
    }

    private Version findVersionByName(String name) {
        for (Version version : versions) {
            if (version.getName().equals(name)) {
                return version;
            }
        }
        return null; // Możesz obsłużyć przypadek, gdy wersja nie zostanie znaleziona
    }

    @FXML
    public void openVersion(ActionEvent event) throws IOException {
        System.out.println("otwarcie wersji");

        VersionView versionView = new VersionView();
        versionView.setParentCollectionName(parentCollectionName);
        versionView.setParentCollectible(parentCollectible);
        versionView.setParentSegment(segment);
        versionView.setVersion(versionComboBox.getValue());

        new SwitchScene().switchScene(event, "versionView", versionView);
    }

    @FXML
    public void addVersion(ActionEvent event) throws IOException {
        System.out.println("dodanie wersji");

        VersionFormWindow versionFormWindow = new VersionFormWindow();
        versionFormWindow.setParentSegment(segment);
        versionFormWindow.setParentCollectionName(parentCollectionName);
        versionFormWindow.setParentCollectible(parentCollectible);

        new SwitchScene().displayWindow("VersionFormWindow", "dodaj wersję", versionFormWindow);
    }

    private void fillForm() {
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
        versionComboBox.setValue(null);
        statusComboBox.setValue("status do pobrania");
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
        fillForm();
    }
}