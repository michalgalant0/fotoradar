package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Collectible;
import com.example.fotoradar.models.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectibleOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public CollectibleOperations() throws SQLException {
    }

    // Pobieranie wszystkich obiektów z bazy
    public ArrayList<Collectible> getAllCollectibles(int parentCollectionId) throws SQLException {
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
                    int statusId = resultSet.getInt("status_id");
                    Status status = StatusManager.getInstance().getStatuses().get(statusId);

                    Collectible collectible = new Collectible(id, title, startDate, finishDate, description, status, parentCollectionId);
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
                    int statusId = resultSet.getInt("status_id");
                    Status status = StatusManager.getInstance().getStatuses().get(statusId);
                    int parentCollectionId = resultSet.getInt("collection_id");


                    return new Collectible(id, title, startDate, finishDate, description, status, parentCollectionId);
                }
            }
        }
        return null; // Obiekt nie został znaleziony
    }

    // Dodawanie obiektu do bazy
    public boolean addCollectible(Collectible collectible) throws SQLException {
        String query = "INSERT INTO Collectible (title, start_date, finish_date, description, status_id, collection_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, collectible.getTitle());
            preparedStatement.setString(2, collectible.getStartDate());
            preparedStatement.setString(3, collectible.getFinishDate());
            preparedStatement.setString(4, collectible.getDescription());
            preparedStatement.setInt(5, collectible.getStatus().getId()-1);
            preparedStatement.setInt(6, collectible.getParentCollectionId());

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
        String query = "UPDATE Collectible SET title=?, start_date=?, finish_date=?, description=?, status_id=? WHERE collectible_id=? AND collection_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, collectible.getTitle());
            // todo do sprawdzenia - prawdopodobnie daty się nie aktualizują
            preparedStatement.setString(2, collectible.getStartDate());
            preparedStatement.setString(3, collectible.getFinishDate());
            preparedStatement.setString(4, collectible.getDescription());
            preparedStatement.setInt(5, collectible.getStatus().getId()-1);

            preparedStatement.setInt(6, collectible.getId());
            preparedStatement.setInt(7, collectible.getParentCollectionId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCollectibleWithSubstructures(int collectibleId) throws SQLException {
        // Usuwanie wszystkich podrędnych miniatur
        new ThumbnailOperations().deleteThumbnailsByCollectibleId(collectibleId);

        // Usuwanie wszystkich podrędnych segmentów
        new SegmentOperations().deleteSegmentsByCollectibleId(collectibleId);

        // Usuwanie wszystkich podrędnych wersji
        new VersionOperations().deleteVersionsByCollectibleId(collectibleId);

        // Usuwanie wszystkich podrędnych zdjęć
        new PhotoOperations().deletePhotosByCollectibleId(collectibleId);

        // Usuwanie obiektu
        String query = "DELETE FROM COLLECTIBLE WHERE collectible_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectibleId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteCollectiblesByCollectionId(int collectionId) throws SQLException {
        String query = "DELETE FROM COLLECTIBLE WHERE collection_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectionId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
