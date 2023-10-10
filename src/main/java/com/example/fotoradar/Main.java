package com.example.fotoradar;

import com.example.fotoradar.databaseOperations.*;
import com.example.fotoradar.models.*;

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
        // K O L E K C J E
        // test klasy do operacji na kolekcjach
        // todo usunąć cały blok
        CollectionOperations collectionOperations = new CollectionOperations(connection);

        System.out.println("K O L E K C J E");

//        System.out.println("pobieranie wszystkich kolekcji");
//        for (Collection collection : collectionOperations.getAllCollections())
//            System.out.println(collection.toString());

        System.out.println("pobieranie jednej kolekcji (id=38)");
        System.out.println(collectionOperations.getCollectionById(38).toString()); // pobieranie nieistniejacego id wywala aplikacje

//        System.out.println("dodawanie kolekcji");
//        System.out.println(collectionOperations.addCollection(
//                new Collection(2, "asdasd", "09.10.2023", "10.10.2023", "asdasdasdas")
//        ));

         System.out.println("aktualizacja kolekcji");
         System.out.println(collectionOperations.updateCollection(
                 new Collection(38, "kolekcja 38 updated", "10.10.2023", "10.10.2023", "opis updated")
         ));

        System.out.println("pobieranie jednej kolekcji (id=38)");
        System.out.println(collectionOperations.getCollectionById(38).toString()); // pobieranie nieistniejacego id wywala aplikacje

        // System.out.println("usuwanie kolekcji");
        // System.out.println(collectionOperations.deleteCollection(1));

//        System.out.println("pobieranie wszsytkich kolekcji");
//        for (Collection collection : collectionOperations.getAllCollections())
//            System.out.println(collection.toString());

        // O B I E K T Y
        CollectibleOperations collectibleOperations = new CollectibleOperations(connection);
        System.out.println("O B I E K T Y");
//        System.out.println("pobieranie wszystkich obiektów");
//        for (Collectible collectible : collectibleOperations.getAllCollectibles())
//            System.out.println(collectible.toString());

        System.out.println("dodawanie obiektu");
//        System.out.println(collectibleOperations.addCollectible(
//                new Collectible(1, "asdasd", "09.10.2023", "10.10.2023", "asdasdasdas", 8)
//        ));
//        System.out.println(collectibleOperations.addCollectible(
//                new Collectible(3, "asdasd", "09.10.2023", "10.10.2023", "asdasdasdas", 8)
//        ));

        System.out.println("aktualizacja obiektu");
        System.out.println(collectibleOperations.updateCollectible(
                new Collectible(14, "obiekt updated", "10.10.2023", "10.10.2023", "opis updated", 8)
        ));

//        System.out.println("pobieranie jednego obiektu (id=14)");
//        System.out.println(collectibleOperations.getCollectibleById(14,8).toString()); // pobieranie nieistniejacego id wywala aplikacje

//        System.out.println("usuwanie obiektu");
//        System.out.println(collectibleOperations.deleteCollectible(1));

        //System.out.println("pobieranie wszystkich obiektów");
        //for (Collectible collectible : collectibleOperations.getAllCollectibles())
        //    System.out.println(collectible.toString());

        // tymczasowe sztywne tworzenie statusow
        // StatusOperations statusOperations = new StatusOperations(connection);
        // System.out.println("dodanie statusow");
        // statusOperations.addStatusValues();

        //

        // S E G M E N T Y
        SegmentOperations segmentOperations = new SegmentOperations(connection);
        System.out.println("S E G M E N T Y");

//        System.out.println("dodawanie segmentu");
//        System.out.println(segmentOperations.addSegment(
//                new Segment(1, "tytul123", "10.10.2023", "10.10.2023", "opis opis",1 ,1)
//        ));


        System.out.println("aktualizacja segmentu");
        System.out.println(segmentOperations.updateSegment(
                new Segment(1, "tytul123 updated", "10.10.2023", "10.10.2023", "opis opis updated",1 ,1)
        ));

        System.out.println("usuwanie segmentu");
        System.out.println(collectibleOperations.deleteCollectible(2));

//        System.out.println("pobieranie jednego segmentu o id=1");
//        System.out.println(segmentOperations.getSegmentById(1,1,1).toString()); // pobieranie nieistniejacego id wywala aplikacje




        // W E R S J E
        VersionOperations versionOperations = new VersionOperations(connection);
        System.out.println("W E R S J E ");

//        System.out.println("dodawanie wersji");
//        System.out.println(versionOperations.addVersion(
//                new Version(1, "nazwa wersji", "10.10.2023", "10.10.2023",1,1)
//        ));
//        System.out.println(versionOperations.addVersion(
//                new Version(2, "nazwa wersji 2", "10.10.2023", "10.10.2023",2,2)
//        ));

        System.out.println("aktualizacja wersji");
        System.out.println(versionOperations.updateVersion(
                new Version(1, "nazwa wersji updated 2", "10.10.2023", "10.10.2023",2 ,1)
        ));

        System.out.println("usuwanie wersji");
        System.out.println(versionOperations.deleteVersion(4));

        System.out.println("pobieranie wszystkich wersji");
        for (Version version : versionOperations.getAllVersions())
            System.out.println(version.toString());

//        System.out.println("pobieranie jednej wersji o id=1");
//        System.out.println(versionOperations.getVersionById(1,2,1).toString()); // pobieranie nieistniejacego id wywala aplikacje



        // Z D J E C I A

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