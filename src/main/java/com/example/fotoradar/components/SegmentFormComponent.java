package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.SwitchScene;
import com.example.fotoradar.databaseOperations.StatusManager;
import com.example.fotoradar.databaseOperations.VersionOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Status;
import com.example.fotoradar.models.Version;
import com.example.fotoradar.views.VersionView;
import com.example.fotoradar.windows.VersionFormWindow;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    public ComboBox<Status> statusComboBox;

    private Segment segment;
    @Setter
    private String parentCollectionName;
    @Setter
    private Collectible parentCollectible;

    private ArrayList<Version> versions;
    private ArrayList<Status> statuses;

    public SegmentFormComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/SegmentFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

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

    public void fillVersionComboBox() throws SQLException {
        versions = new VersionOperations().getAllVersions(segment.getId());

        versionComboBox.setConverter(new StringConverter<>() {
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
        versionFormWindow.setOnWindowClosedListener(this::loadVersions);

        new SwitchScene().displayWindow("VersionFormWindow", "dodaj wersję", versionFormWindow);
    }

    public void fillForm() {
        numberTextField.setText(String.valueOf(segment.getId()));

        String title = segment.getTitle();

        // Przykład sprawdzenia i wyświetlenia komunikatu w miejscu pola tytułu
        if (title.isEmpty()) {
            // Pobranie wcześniej utworzonego pola tekstowego do wprowadzania tytułu
            TextField titleTextField = nameTextField;

            // Ustawienie czerwonej ramki lub tła dla pola tytułu jako wskazanie błędu
            titleTextField.setStyle("-fx-border-color: red;"); // Możesz dostosować to według potrzeb

            // Wstawienie komunikatu w miejscu pola tytułu
            titleTextField.setPromptText("Pole tytułu nie może być puste!");
            return;
        } else {
            // Jeśli tytuł nie jest pusty, przywróć domyślny styl pola tekstowego
            TextField titleTextField = nameTextField;
            titleTextField.setStyle(""); // Usunięcie dodanego stylu (reset do domyślnego)

            // Możesz także usunąć komunikat, jeśli taki został wyświetlony
            titleTextField.setPromptText(""); // Usunięcie wyświetlonego komunikatu
        }
        nameTextField.setText(title);

        String startDate = segment.getStartDate();
        if (startDate != null && !startDate.isEmpty()) {
            startDatePicker.setValue(LocalDate.parse(startDate.substring(0, 10)));
            startTimeTextField.setText(extractTimeFromDate(startDate)); // Extracting time part
        } else {
            startDatePicker.setValue(null);
            startTimeTextField.setText(null);
        }

        String finishDate = segment.getFinishDate();
        if (finishDate != null && !finishDate.isEmpty()) {
            finishDatePicker.setValue(LocalDate.parse(finishDate.substring(0, 10)));
            finishTimeTextField.setText(extractTimeFromDate(finishDate)); // Extracting time part
        } else {
            finishDatePicker.setValue(null);
            finishTimeTextField.setText(null);
        }

        String description = segment.getDescription();
        if (description != null && !description.isEmpty()) {
            descriptionTextArea.setText(description);
        } else {
            descriptionTextArea.setText(null);
        }

        if (segment.getStatus() == null) {
            // wartosc domyslna - zacheta
            Status tmpStatus = new Status();
            tmpStatus.setName("wybierz status");
            statusComboBox.setValue(tmpStatus);
        } else
            statusComboBox.setValue(segment.getStatus());

        try {
            fillVersionComboBox();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractTimeFromDate(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty()) {
            try {
                LocalDateTime dateTimeValue = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                return dateTimeValue.format(DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                // Obsługa przypadku, gdy nie można sparsować czasu (brak godziny)
                LocalDate dateValue = LocalDate.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return ""; // Zwrócenie pustego ciągu znaków dla samej daty
            }
        }
        return null; // Zwrócenie wartości null, jeśli przekazany ciąg znaków jest pusty
    }


    public void setSegment(Segment segment) {
        this.segment = segment;
        fillForm();

        loadVersions();
    }

    private void loadVersions() {
        try {
            fillVersionComboBox();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
