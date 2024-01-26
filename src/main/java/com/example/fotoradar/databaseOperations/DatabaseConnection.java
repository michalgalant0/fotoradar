package com.example.fotoradar.databaseOperations;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
public class DatabaseConnection {
    private static final String DATABASE_FILE_NAME = ".fotoradar.db"; // Zmiana na nazwę pliku
    private static final String DATABASE_FILE_PATH = "./" + DATABASE_FILE_NAME; // Ścieżka względna

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
        return new File(DATABASE_FILE_PATH).exists();
    }

    private void createDatabase() throws IOException, SQLException {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             Statement statement = connection.createStatement()) {

            // Ładowanie pliku SQL z zasobów JAR
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("fotoradar_database.sql");

            if (inputStream != null) {
                String sqlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("Executing SQL script...");
                statement.executeUpdate(sqlContent);
                System.out.println("SQL script executed successfully!");
            } else {
                System.out.println("Nie można znaleźć pliku SQL w zasobach JAR.");
            }
        }
    }

    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                System.out.println(databaseExists());
                if (!databaseExists()) {
                    createDatabase();
                }
            } catch (IOException e) {
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
