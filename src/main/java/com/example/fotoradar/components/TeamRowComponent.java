package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.models.Team;
import com.example.fotoradar.windows.ConfirmDeletePopup;
import com.example.fotoradar.RemoveStructureListener;
import com.example.fotoradar.SwitchScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TeamRowComponent extends AnchorPane {
    @FXML
    public Label nameLabel;
    @FXML
    public Label descriptionLabel;

    @Setter
    private Collectible collectible;

    @Setter
    private Team team;

    public TeamRowComponent() throws IOException{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamRowComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }
    public void initialize() {
    }
    public void setNameLabel(String name) {
        nameLabel.setText(name);
    }

    public void setDescriptionLabel(String description) {
        descriptionLabel.setText(description);
    }

}
