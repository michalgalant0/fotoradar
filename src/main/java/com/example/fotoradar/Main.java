package com.example.fotoradar;

import com.example.fotoradar.databaseOperations.CollectibleOperations;
import com.example.fotoradar.databaseOperations.DatabaseConnection;
import com.example.fotoradar.databaseOperations.SegmentOperations;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage)
            throws IOException, SQLException
    {

        // testowanie nawiązywania połączenia z bazą
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();

        // test operacji
        // utworzenie obiektu klasy ..Operations
        CollectibleOperations cop = new CollectibleOperations(connection);
        // wydrukowanie w konsoli zwrotu danej metody
        System.out.println(
                cop.getCollectibleById(3, 8).toString()
        );

        SegmentOperations sop = new SegmentOperations(connection);
        System.out.println(sop.getSegmentById(1,1,1).toString());



        // using fxml loader for layout loading
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("collections-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("fotoradar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}