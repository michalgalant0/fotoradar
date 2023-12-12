package com.example.fotoradar.windows;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.components.VersionFormComponent;
import com.example.fotoradar.databaseOperations.VersionOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Version;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VersionFormWindow implements Window, OnWindowClosedListener {
    @FXML
    public Label windowLabel;
    @FXML
    public VersionFormComponent versionForm;

    @Setter
    private Stage dialogStage;

    @Setter
    private String parentCollectionName;
    @Setter
    private Collectible parentCollectible;
    @Setter
    private Segment parentSegment;

    @Setter
    private OnWindowClosedListener onWindowClosedListener;

    public void initialize() {
        System.out.println("VersionFormWindow.parentSegment: "+parentSegment);
        System.out.println(parentCollectionName);
        System.out.println(parentCollectible);
        versionForm.setParentCollectionId(parentCollectible.getParentCollectionId());
        try {
            versionForm.fillTeamComboBox();
            versionForm.setOnWindowClosedListener(this);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setWindowLabel();
    }

    private void setWindowLabel() {
        windowLabel.setText(String.format("Dodaj wersję do segmentu %s", parentSegment.getTitle()));
    }
    private String mergeTimeIntoDatePicker(DatePicker datePicker, TextField timeTextField) {
        String timeText = timeTextField.getText();

        if (datePicker == null || datePicker.getValue() == null) {
            return null;
        }

        LocalDate date = datePicker.getValue();

        if (timeText == null || timeText.isEmpty()) {
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        LocalTime time = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime dateTime = date.atTime(time);
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }


    @FXML
    public void saveVersion(ActionEvent event) throws SQLException {
        System.out.println("zapisz wersję");

        String title = versionForm.nameTextField.getText();

        // Przykład sprawdzenia i wyświetlenia komunikatu w miejscu pola tytułu
        if (title.isEmpty()) {
            // Pobranie wcześniej utworzonego pola tekstowego do wprowadzania tytułu
            TextField titleTextField = versionForm.nameTextField;

            // Ustawienie czerwonej ramki lub tła dla pola tytułu jako wskazanie błędu
            titleTextField.setStyle("-fx-border-color: red;"); // Możesz dostosować to według potrzeb

            // Wstawienie komunikatu w miejscu pola tytułu
            titleTextField.setPromptText("Pole nazwy nie może być puste!");
            return;
        }

        String startTime = mergeTimeIntoDatePicker(versionForm.startDatePicker, versionForm.startTimeTextField);
        String finishTime = mergeTimeIntoDatePicker(versionForm.finishDatePicker, versionForm.finishTimeTextField);

        Version versionToAdd = new Version(
                title,
                startTime,
                finishTime,
                versionForm.descriptionTextArea.getText(),
                versionForm.teamComboBox.getValue().getId(),
                parentSegment.getId()
        );

        System.out.println("dane z formularza: " + versionToAdd);

        // dodanie oibiektu do bazy
        VersionOperations versionOperations = new VersionOperations();
        versionOperations.addVersion(versionToAdd);

        // utworzenie katalogu wersji
        DirectoryOperator.getInstance().createStructure(versionToAdd, parentCollectionName, parentCollectible.getTitle(), parentSegment.getTitle());

        // po wykonaniu operacji zamknij okienko
        onWindowClosedListener.onWindowClosed();
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }

    @Override
    public void onWindowClosed() {
        try {
            versionForm.fillTeamComboBox();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
