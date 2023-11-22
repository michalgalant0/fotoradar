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
public class TeamFormComponent extends AnchorPane{
    @FXML
    public TextField nameTextField;

    @FXML
    public TextArea descriptionTextArea;

    public TeamFormComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamFormComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

}
