package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Collectible;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectibleOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public CollectibleOperations() throws SQLException {
    }

    // pobranie wszystkich obiektów z bazy
    public ArrayList<Collectible> getAllCollectibles() throws SQLException {
        ArrayList<Collectible> collectibles = new ArrayList<>();
        String query = "SELECT * FROM Collectible;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("collectible_id");
                    String title = resultSet.getString("title");
                    String startDate = resultSet.getString("start_date");
                    String finishDate = resultSet.getString("finish_date");
                    String description = resultSet.getString("description");
                    int parentId = resultSet.getInt("collection_id");

                    Collectible collectible = new Collectible(id, title, startDate, finishDate, description, parentId);
                    collectibles.add(collectible);
                }
            }
        }
        return collectibles;
    }

    // Pobieranie wszystkich obiektów z bazy dla danej kolekcji
    public ArrayList<Collectible> getAllCollectiblesByParentId(int parentCollectionId) throws SQLException {
        ArrayList<Collectible> collectibles = new ArrayList<>();
        String query = "SELECT * FROM Collectible WHERE collection_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, parentCollectionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("collectible_id");
                    String title = resultSet.getString("title");
                    String startDate = resultSet.getString("start_date");
                    String finishDate = resultSet.getString("finish_date");
                    String description = resultSet.getString("description");

                    Collectible collectible = new Collectible(id, title, startDate, finishDate, description, parentCollectionId);
                    collectibles.add(collectible);
                }
            }
        }
        return collectibles;
    }


    // Pobieranie jednego obiektu z bazy po ID
    public Collectible getCollectibleById(int id) throws SQLException {
        String query = "SELECT * FROM Collectible WHERE collectible_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String startDate = resultSet.getString("start_date");
                    String finishDate = resultSet.getString("finish_date");
                    String description = resultSet.getString("description");
                    int parentCollectionId = resultSet.getInt("collection_id");


                    return new Collectible(id, title, startDate, finishDate, description, parentCollectionId);
                }
            }
        }
        return null; // Obiekt nie został znaleziony
    }

    // Dodawanie obiektu do bazy
    public boolean addCollectible(Collectible collectible) throws SQLException {
        String query = "INSERT INTO Collectible (title, start_date, finish_date, description, collection_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, collectible.getTitle());
            preparedStatement.setString(2, collectible.getStartDate());
            preparedStatement.setString(3, collectible.getFinishDate());
            preparedStatement.setString(4, collectible.getDescription());
            preparedStatement.setInt(5, collectible.getParentCollectionId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Usuwanie obiektu z bazy po ID
    public boolean deleteCollectible(int id) throws SQLException {
        String query = "DELETE FROM Collectible WHERE collectible_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Aktualizacja istniejącego obiektu w bazie
    public boolean updateCollectible(Collectible collectible) throws SQLException {
        String query = "UPDATE Collectible SET title=?, start_date=?, finish_date=?, description=? WHERE collectible_id=? AND collection_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, collectible.getTitle());
            preparedStatement.setString(2, collectible.getStartDate());
            preparedStatement.setString(3, collectible.getFinishDate());
            preparedStatement.setString(4, collectible.getDescription());

            preparedStatement.setInt(5, collectible.getId());
            preparedStatement.setInt(6, collectible.getParentCollectionId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }


}
