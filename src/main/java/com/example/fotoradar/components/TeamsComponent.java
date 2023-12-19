package com.example.fotoradar.components;

import com.example.fotoradar.Main;
import com.example.fotoradar.TeamsComponentFlag;
import com.example.fotoradar.databaseOperations.TeamOperations;
import com.example.fotoradar.models.Team;
import com.example.fotoradar.views.ParametersView;
import com.example.fotoradar.views.TeamsView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.Setter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamsComponent extends AnchorPane {
    @Setter
    private Object parentView;
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
        teamsContainer.getChildren().clear();
        System.err.println("TeamsComponent.flag "+flag);
        if (flag == TeamsComponentFlag.TEAMS_VIEW)
            MAX_COLUMNS = 4;
        else if (flag == TeamsComponentFlag.PARAMETERS_VIEW)
            MAX_COLUMNS = 3;

        // szerokości komponentów TeamComponent + odstępy między nimi z marginesami + margines prawy
        teamsContainer.setPrefWidth(MAX_COLUMNS*(new TeamComponent().getPrefWidth())+(MAX_COLUMNS+1)*24.0);

        if (teams == null || teams.isEmpty()) {
            Label infoLabel = new Label("NIE MA ŻADNEGO ZESPOŁU");
            infoLabel.setStyle("""
                    -fx-font-size: 20;
                    -fx-text-fill: grey;
                    -fx-alignment: center;
                    """);
            teamsContainer.setAlignment(Pos.CENTER);
            teamsContainer.getChildren().add(infoLabel);
        } else {
            fillComponent();
        }
    }

    private void fillComponent() throws IOException {
        teamsContainer.setAlignment(Pos.TOP_LEFT);
        System.out.println(teams);
        int columnIndex = 0;
        int rowIndex = 0;

        for (Team team : teams) {
            System.out.println("TeamsComponent.fillComponent: team: "+team);
            //utworzenie komponentu do dodania
            TeamComponent teamComponent = new TeamComponent();
            teamComponent.setTeam(team);
            teamComponent.fillComponent();
            if (flag == TeamsComponentFlag.PARAMETERS_VIEW) {
                teamComponent.setRightClickListener((ParametersView)parentView);
                teamComponent.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        System.out.println("kliknieto prawym przyciskiem na: " + team);
                        showContextMenu(teamComponent, team);
                    }
                });
            }
            else if (flag == TeamsComponentFlag.TEAMS_VIEW) {
                teamComponent.setLeftClickListener((TeamsView)parentView);
                teamComponent.setRightClickListener((TeamsView)parentView);
                teamComponent.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                        System.out.println("kliknieto lewym przyciskiem na: " + team);
                        ((TeamsView) parentView).onTeamComponentClicked(team);
                    }
                    else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        System.out.println("kliknieto prawym przyciskiem na: " + team);
                        showContextMenu(teamComponent, team);
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

    private void showContextMenu(TeamComponent teamComponent, Team team) {
        ContextMenu contextMenu = new ContextMenu();

        // Dodanie opcji "USUŃ" do menu kontekstowego
        MenuItem deleteMenuItem = new MenuItem("USUŃ");
        deleteMenuItem.setOnAction(actionEvent -> {
            System.out.println("USUWANIE ZESPOLU " + team.getName());
            try {
                new TeamOperations().deleteTeam(team.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (parentView instanceof ParametersView)
                ((ParametersView) parentView).onDeletePerformed();
            else if (parentView instanceof TeamsView)
                ((TeamsView) parentView).onDeletePerformed();
        });

        contextMenu.getItems().add(deleteMenuItem);
        // Wyświetlenie menu kontekstowego w miejscu kliknięcia
        contextMenu.show(teamComponent, teamComponent.localToScreen(0, 0).getX(), teamComponent.localToScreen(0, 0).getY());

        // Dodanie obsługi zdarzenia dla kliknięcia gdziekolwiek indziej, aby schować menu kontekstowe
        teamComponent.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                contextMenu.hide();
            }
        });
    }
}
