package com.example.fotoradar.windows;

import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.components.VersionFormComponent;
import com.example.fotoradar.databaseOperations.VersionOperations;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Segment;
import com.example.fotoradar.models.Version;
import com.example.fotoradar.models.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;

public class VersionFormWindow implements Window {
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

    public void initialize() {
        System.out.println("VersionFormWindow.parentSegment: "+parentSegment);
        //System.out.println("VersionFormWindow.parentTeam: "+parentTeam);
        System.out.println(parentCollectionName);
        System.out.println(parentCollectible);
        setWindowLabel();
    }

    private void setWindowLabel() {
        windowLabel.setText(String.format("Dodaj wersję do segmentu %s", parentSegment.getTitle()));
    }

    @FXML
    public void saveVersion(ActionEvent event) throws SQLException {
        System.out.println("zapisz wersję");

        Version versionToAdd = new Version(
                versionForm.nameTextField.getText(),
                versionForm.startDatePicker.getValue().toString(),
                versionForm.finishDatePicker.getValue().toString(),
                versionForm.descriptionTextArea.getText(),
                // todo dodac przekazywanie id zespolu
                1,
                parentSegment.getId()
        );

        System.out.println("dane z formularza: " + versionToAdd);

        // dodanie oibiektu do bazy
        VersionOperations versionOperations = new VersionOperations();
        versionOperations.addVersion(versionToAdd);

        // utworzenie katalogu wersji
        DirectoryOperator.getInstance().createStructure(versionToAdd, parentCollectionName, parentCollectible.getTitle(), parentSegment.getTitle());

        // po wykonaniu operacji zamknij okienko
        closeWindow(dialogStage);
    }

    @FXML
    public void cancel(ActionEvent event) {
        System.out.println("anuluj");
        closeWindow(dialogStage);
    }
}
