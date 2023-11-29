package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.TeamsComponentFlag;
import com.example.fotoradar.models.Team;
import com.example.fotoradar.views.TeamsView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

public class TeamsComponent extends AnchorPane {
    @Setter
    private TeamsView parentView;
    @FXML
    public GridPane teamsContainer;
    @FXML
    public ScrollPane scrollPane;

    @Setter
    private ArrayList<Team> teams;
    @Setter
    private TeamsComponentFlag flag;
    private int MAX_COLUMNS;

    public TeamsComponent() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/TeamsComponent.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    public void fillTeamsComponent() throws IOException {
        System.err.println("TeamsComponent.flag "+flag);
        if (flag == TeamsComponentFlag.TEAMS_VIEW)
            MAX_COLUMNS = 4;
        else if (flag == TeamsComponentFlag.PARAMETERS_VIEW)
            MAX_COLUMNS = 3;

        // szerokości komponentów TeamComponent + odstępy między nimi z marginesami + margines prawy
        teamsContainer.setPrefWidth(MAX_COLUMNS*326.0+(MAX_COLUMNS+2)*20.0);

        teamsContainer.getChildren().clear();
        System.out.println(teams);
        int columnIndex = 0;
        int rowIndex = 0;

        for (Team team : teams) {
            System.out.println("TeamsComponent.fillComponent: team: "+team);
            //utworzenie komponentu do dodania
            TeamComponent teamComponent = new TeamComponent();
            teamComponent.setTeam(team);
            teamComponent.fillComponent();
            if (flag == TeamsComponentFlag.TEAMS_VIEW) {
                teamComponent.setLeftClickListener(parentView);
                teamComponent.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        System.out.println("kliknieto lewym przyciskiem na: " + team);
                        parentView.onTeamComponentClicked(team);
                    }
                    //todo usuwanie na prawym kliku
                    else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        System.out.println("kliknieto prawym przyciskiem na: " + team);
                    }
                });
            }
            //dodanie komponentu
            teamsContainer.add(teamComponent, columnIndex, rowIndex);
            // Przesuwaj się do kolejnej kolumny lub wiersza
            columnIndex++;
            if (columnIndex >= MAX_COLUMNS) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }
}
