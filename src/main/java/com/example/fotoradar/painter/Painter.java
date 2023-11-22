package com.example.fotoradar.painter;

import com.example.fotoradar.Main;
import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Collection;
import com.example.fotoradar.views.PainterView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;


public class Painter extends Application {

    @Setter
    private Collection parentCollection;
    @Setter
    private Collectible collectible;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/painterView.fxml"));
        PainterView painterView = new PainterView();
        painterView.setCollectible(collectible);
        painterView.setParentCollection(parentCollection);
        fxmlLoader.setController(painterView);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        // stare dziala
//        Parent root = FXMLLoader.load(Main.class.getResource("views/painterView.fxml"));
//        Scene scene = new Scene(root);
//
//        stage.setScene(scene);
//        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
