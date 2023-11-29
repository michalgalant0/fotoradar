package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import lombok.Setter;

import java.io.IOException;

public class TeamComponent extends AnchorPane {
    @FXML
    private Label nameLabel;
    @FXML
    private TextArea descriptionTextArea;

    @Setter
    private Team team;

    @Setter
    private TeamComponentRightClickListener rightClickListener;
    @Setter
    private TeamComponentLeftClickListener leftClickListener;

    public TeamComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillComponent() {
        setNameLabel();
        setDescriptionTextArea();
    }

    private void setNameLabel() {
        nameLabel.setText(team.getName());
    }

    private void setDescriptionTextArea() {
        descriptionTextArea.setText(team.getDescription());
    }
}
