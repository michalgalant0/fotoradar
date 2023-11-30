package com.example.fotoradar;

import com.example.fotoradar.windows.OnWindowClosedListener;
import com.example.fotoradar.windows.Window;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SwitchScene {
    public void switchScene (ActionEvent event, String viewName) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/" + viewName + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public void switchScene(ActionEvent event, String viewName, Object controller) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/" + viewName + ".fxml"));
        fxmlLoader.setController(controller); // Ustawienie kontrolera widoku
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public void displayWindow(String windowName, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("windows/"+windowName+".fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.DECORATED);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle(title);

        // Ustawienie sceny na nowym oknie
        dialogStage.setScene(scene);

        // Pobranie kontrolera
        Window controller = fxmlLoader.getController();
        controller.setDialogStage(dialogStage);

        //Platform.runLater(() -> {
        //    dialogStage.setMaximized(true);
        //    // Możesz również ustawić stage.setFullScreen(true); jeśli faktycznie chcesz pełen ekran
        //});
        //setFullScreen(dialogStage, true);

        // Wyświetlenie nowego okna
        dialogStage.show();

        dialogStage.toFront();
        dialogStage.requestFocus();
    }

    public void displayWindow(String windowName, String title, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("windows/" + windowName + ".fxml"));
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());

        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.DECORATED);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle(title);
        dialogStage.setScene(scene);

        // Jeśli controller implementuje Window, przekaż dialogStage
        if (controller instanceof Window) {
            ((Window) controller).setDialogStage(dialogStage);
        }
        //Platform.runLater(() -> {
        //    dialogStage.setMaximized(true);
        //    // Możesz również ustawić stage.setFullScreen(true); jeśli faktycznie chcesz pełen ekran
        //});
        //setFullScreen(dialogStage, true);
        // Wyświetlenie nowego okna
        dialogStage.show();

        dialogStage.toFront();
        dialogStage.requestFocus();
    }

    public void displayWindow(String windowName, String title, Object controller, OnWindowClosedListener listener) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("windows/" + windowName + ".fxml"));
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());

        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.DECORATED);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle(title);
        dialogStage.setScene(scene);

        // Jeśli controller implementuje Window, przekaż dialogStage
        //if (controller instanceof Window) {
        //    ((Window) controller).setDialogStage(dialogStage);
        //}
        //// Platform.runLater jest używany do maksymalizacji okna po załadowaniu sceny
        //Platform.runLater(() -> {
        //    dialogStage.setMaximized(true);
        //    // Możesz również ustawić stage.setFullScreen(true); jeśli faktycznie chcesz pełen ekran
        //});

        // Jeśli controller implementuje Window, przekaż dialogStage

        // Wyświetlenie nowego okna
        dialogStage.show();

        dialogStage.toFront();
        dialogStage.requestFocus();
        dialogStage.setOnCloseRequest(windowEvent -> listener.onWindowClosed());
    }
    public static void setFullScreen(Stage stage, boolean fullScreen) {
        if (stage != null) {
            stage.setFullScreen(fullScreen);
        }
    }
}
