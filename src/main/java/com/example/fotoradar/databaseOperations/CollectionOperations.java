package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Collection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionOperations {
    private Connection connection;

    public CollectionOperations(Connection connection) {
        this.connection = connection;
    }

    // Pobieranie wszystkich kolekcji z bazy
    public List<Collection> getAllCollections() throws SQLException {
        List<Collection> collections = new ArrayList<>();
        String query = "SELECT * FROM Collection";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("collection_id");
                String title = resultSet.getString("title");
                String startDate = resultSet.getString("start_date");
                String finishDate = resultSet.getString("finish_date");
                String description = resultSet.getString("description");

                Collection collection = new Collection(id, title, startDate, finishDate, description);
                collections.add(collection);
            }
        }
        return collections;
    }

    // Pobieranie jednej kolekcji z bazy po ID
    public Collection getCollectionById(int id) throws SQLException {
        String query = "SELECT * FROM Collection WHERE collection_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String startDate = resultSet.getString("start_date");
                    String finishDate = resultSet.getString("finish_date");
                    String description = resultSet.getString("description");

                    return new Collection(id, title, startDate, finishDate, description);
                }
            }
        }
        return null; // Kolekcja nie została znaleziona
    }

    // Dodawanie kolekcji do bazy
    public boolean addCollection(Collection collection) throws SQLException {
        String query = "INSERT INTO Collection (title, start_date, finish_date, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, collection.getTitle());
            preparedStatement.setString(2, collection.getStartDate());
            preparedStatement.setString(3, collection.getFinishDate());
            preparedStatement.setString(4, collection.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Usuwanie kolekcji z bazy po ID
    public boolean deleteCollection(int id) throws SQLException {
        String query = "DELETE FROM Collection WHERE collection_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Aktualizacja istniejącej kolekcji w bazie
    public boolean updateCollection(Collection collection) throws SQLException {
        String query = "UPDATE Collection SET title=?, start_date=?, finish_date=?, description=? WHERE collection_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, collection.getTitle());
            preparedStatement.setString(2, collection.getStartDate());
            preparedStatement.setString(3, collection.getFinishDate());
            preparedStatement.setString(4, collection.getDescription());

            preparedStatement.setInt(5, collection.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
