package com.example.fotoradar;

import com.example.fotoradar.windows.OnWindowClosedListener;
import com.example.fotoradar.windows.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SwitchScene {
    private static SwitchScene instance;
    private static int[] prefRes;

    private SwitchScene() {
    }

    public static synchronized SwitchScene getInstance() {
        if (instance == null) {
            instance = new SwitchScene();
            prefRes = Main.getPrefResolution();
        }
        return instance;
    }
    public void switchScene (ActionEvent event, String viewName) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(prefRes[0]);
        stage.setHeight(prefRes[1]);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/" + viewName + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public void switchScene(ActionEvent event, String viewName, Object controller) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(prefRes[0]);
        stage.setHeight(prefRes[1]);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/" + viewName + ".fxml"));
        fxmlLoader.setController(controller); // Ustawienie kontrolera widoku
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public void displayWindow(String windowName, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("windows/"+windowName+".fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage dialogStage = new Stage();

        // testowe, zeby mi uruchamialo na drugim monitorze XD
//        //todo usunac
//        Screen secondaryScreen = Screen.getScreens().get(1);
//        Rectangle2D bounds = secondaryScreen.getVisualBounds();
//        // Ustawienie położenia sceny na drugim monitorze
//        dialogStage.setX(bounds.getMinX());
//        dialogStage.setY(bounds.getMinY());
//        //todo koniec usuniecia

        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);

        // Ustawienie sceny na nowym oknie
        dialogStage.setScene(scene);

        // Pobranie kontrolera
        Window controller = fxmlLoader.getController();
        controller.setDialogStage(dialogStage);

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

        // testowe, zeby mi uruchamialo na drugim monitorze XD
//        //todo usunac
//        Screen secondaryScreen = Screen.getScreens().get(1);
//        Rectangle2D bounds = secondaryScreen.getVisualBounds();
//        // Ustawienie położenia sceny na drugim monitorze
//        dialogStage.setX(bounds.getMinX());
//        dialogStage.setY(bounds.getMinY());
//        //todo koniec usuniecia

        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);
        dialogStage.setScene(scene);

        // Jeśli controller implementuje Window, przekaż dialogStage
        if (controller instanceof Window) {
            ((Window) controller).setDialogStage(dialogStage);
        }

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
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);
        dialogStage.setScene(scene);

        // Jeśli controller implementuje Window, przekaż dialogStage
        if (controller instanceof Window) {
            ((Window) controller).setDialogStage(dialogStage);
        }

        // Wyświetlenie nowego okna
        dialogStage.show();

        dialogStage.toFront();
        dialogStage.requestFocus();
        dialogStage.setOnCloseRequest(windowEvent -> listener.onWindowClosed());
    }
}
