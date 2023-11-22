package com.example.fotoradar.views;

import com.example.fotoradar.components.CollectionComponent;
import com.example.fotoradar.components.TeamComponent;
import com.example.fotoradar.components.TeamFormComponent;
import com.example.fotoradar.models.Team;
import com.example.fotoradar.windows.ConfirmDeletePopup;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.components.TeamsComponent;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.DirectoryOperator;
import com.example.fotoradar.RemoveStructureListener;
import com.example.fotoradar.SwitchScene;

import com.example.fotoradar.windows.ConfirmDeletePopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
public class TeamsView {

    @FXML
    public TeamFormComponent teamFormComponent;
    @FXML
    public TeamComponent teamComponent;
    @FXML
    public CollectionComponent collectionComponent;
    @FXML
    public Label windowLabel;
    @Setter
    private Team team;


}
