package com.example.fotoradar;

import com.example.fotoradar.databaseOperations.CollectionOperations;
import com.example.fotoradar.databaseOperations.DatabaseConnection;
import com.example.fotoradar.models.Collection;
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

        // test klasy do operacji na kolekcjach
        // todo usunąć cały blok
        CollectionOperations collectionOperations = new CollectionOperations(connection);

        System.out.println("pobieranie wszsytkich");
        for (Collection col : collectionOperations.getAllCollections())
            System.out.println(col.toString());

        System.out.println("pobieranie jednej (id=4)");
        System.out.println(collectionOperations.getCollectionById(4).toString()); // pobieranie nieistniejacego id wywala aplikacje

        System.out.println("dodawanie");
        System.out.println(collectionOperations.addCollection(
                new Collection(2, "asdasd", "09.10.2023", "10.10.2023", "asdasdasdas")
        ));

        System.out.println("aktualizacja");
        System.out.println(collectionOperations.updateCollection(
                new Collection(1, "asd updated", "09.10.2023", "10.10.2023", "desccc updated")
        ));

        System.out.println("usuwanie");
        System.out.println(collectionOperations.deleteCollection(1));
        // -- koniec testu operacji - do debugowania

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