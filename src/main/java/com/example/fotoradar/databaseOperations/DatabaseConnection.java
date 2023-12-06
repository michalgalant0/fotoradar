package com.example.fotoradar.databaseOperations;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasa realizująca połączenie z bazą danych.
 * Na zasadzie singleton - jedna instancja dla całej aplikacji.
 */
@Getter
public class DatabaseConnection {
    //todo jak to jest z bazą
    //  - jest w resources i ładujemy ją zawsze bezpośrednio z aplikacji
    //  - czy przerzucamy razem z katalogami, albo wymagamy, żeby zawsze była "obok" aplikacji
    private static final String DATABASE_FILE_PATH = System.getProperty("user.dir") + File.separator + "fotoradar.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DATABASE_FILE_PATH;

    private Connection connection;
    private static DatabaseConnection instance;

    private DatabaseConnection() throws SQLException {
        connect();
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null)
            instance = new DatabaseConnection();
        return instance;
    }

    private boolean databaseExists() {
        return Files.exists(Paths.get(DATABASE_FILE_PATH));
    }

    private void createDatabase() throws IOException, SQLException {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             Statement statement = connection.createStatement()) {

            String sqlContent = new String(Files.readAllBytes(Paths.get("fotoradar_database.sql")));
            System.out.println("Executing SQL script...");
            //System.out.println(sqlContent);
            statement.executeUpdate(sqlContent);
            //System.out.println("SQL script executed successfully!");
        }
    }


    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                if (!databaseExists()) {
                    createDatabase();
                }
            } catch (IOException e) {
                // Obsługa błędu odczytu pliku SQL
                e.printStackTrace();
            }

            connection = DriverManager.getConnection(CONNECTION_STRING);
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
