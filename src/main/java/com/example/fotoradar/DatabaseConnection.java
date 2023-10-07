package com.example.fotoradar;

import lombok.Getter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
/**
 Klasa realizująca połączenie z bazą danych.
 Na zasadzie singleton - jedna instancja dla całej aplikacji.
 */
public class DatabaseConnection {
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + getDatabaseFilePath();
    // Metoda do pobierania połączenia do bazy danych
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

    // metoda do utworzenia scieżki do pliku z bazą danych niezależnie od systemu
    private static String getDatabaseFilePath() {
        return System.getProperty("user.dir") + File.separator + "fotoradar.db";
    }

    // metoda do nawiązywania połączenia z bazą danych
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(CONNECTION_STRING);
        }
    }

    // metoda do zamykania połączenia z bazą danych
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}