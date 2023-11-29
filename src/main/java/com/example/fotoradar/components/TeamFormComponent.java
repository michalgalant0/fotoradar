package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Team;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class TeamFormComponent extends AnchorPane {
    @FXML
    public TextField nameTextField;
    @FXML
    public Label teamFormLabel;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public Button clearFormButton;

    @Setter @Getter
    private Team team;

    @Setter
    private TeamComponentLeftClickListener leftClickListener;

    public TeamFormComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillForm() {
        nameTextField.setText(team.getName());
        descriptionTextArea.setText(team.getDescription());
    }

    @FXML
    public void clearForm() {
        teamFormLabel.setText("Dodaj zespół");
        nameTextField.clear();
        descriptionTextArea.clear();
        team = null;
        clearFormButton.setVisible(false);
        leftClickListener.onFormCleared();
    }
}
