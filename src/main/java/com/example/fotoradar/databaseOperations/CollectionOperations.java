package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public CollectionOperations() throws SQLException {
    }

    // Pobieranie wszystkich kolekcji z bazy
    public ArrayList<Collection> getAllCollections() throws SQLException {
        ArrayList<Collection> collections = new ArrayList<>();
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

    public boolean deleteCollectionWithSubstructures(int collectionId) throws SQLException {
        // Usuwanie wszystkich podrzędnych obiektów
        new CollectibleOperations().deleteCollectiblesByCollectionId(collectionId);

        // Usuwanie wszystkich podrędnych miniatur
        new ThumbnailOperations().deleteThumbnailsByCollectionId(collectionId);

        // Usuwanie wszystkich podrędnych segmentów
        new SegmentOperations().deleteSegmentsByCollectionId(collectionId);

        // Usuwanie wszystkich podrędnych wersji
        new VersionOperations().deleteVersionsByCollectionId(collectionId);

        // Usuwanie wszystkich podrędnych zdjęć
        new PhotoOperations().deletePhotosByCollectionId(collectionId);

        // Usuwanie kolekcji
        String query = "DELETE FROM COLLECTION WHERE collection_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectionId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public double[] fetchProgressAndSizeInfo(int collectionId) throws SQLException {
        double[] result = new double[2];

        // Obliczanie postępu w skanowaniu kolekcji
        String progressQuery = """
                WITH CompletedSegments AS (
                    SELECT COUNT(*) AS completed_segments
                    FROM SEGMENT\s
                    WHERE status_id = 3\s
                      AND collectible_id IN (SELECT collectible_id FROM COLLECTIBLE WHERE collection_id = ?)
                ),
                AllSegments AS (
                    SELECT COUNT(*) AS all_segments
                    FROM SEGMENT\s
                    WHERE collectible_id IN (SELECT collectible_id FROM COLLECTIBLE WHERE collection_id = ?)
                )
                SELECT completed_segments, all_segments
                FROM CompletedSegments, AllSegments;
                """;
        try (PreparedStatement progressStatement = connection.prepareStatement(progressQuery)) {
            progressStatement.setInt(1, collectionId);
            progressStatement.setInt(2, collectionId);
            ResultSet progressResultSet = progressStatement.executeQuery();

            if (progressResultSet.next()) {
                int completedSegments = progressResultSet.getInt(1);
                int totalSegments = progressResultSet.getInt(2);

                if (totalSegments > 0) {
                    result[0] = ((double) completedSegments / totalSegments) * 100.0;
                }
            }
        }

        // Obliczanie sumy wag zdjęć
        String sizeQuery = "SELECT SUM(file_size) AS totalSize " +
                "FROM PHOTO " +
                "INNER JOIN VERSION ON PHOTO.version_id = VERSION.version_id " +
                "INNER JOIN SEGMENT ON VERSION.segment_id = SEGMENT.segment_id " +
                "INNER JOIN COLLECTIBLE ON SEGMENT.collectible_id = COLLECTIBLE.collectible_id " +
                "WHERE COLLECTIBLE.collection_id = ?";
        try (PreparedStatement sizeStatement = connection.prepareStatement(sizeQuery)) {
            sizeStatement.setInt(1, collectionId);
            ResultSet sizeResultSet = sizeStatement.executeQuery();

            if (sizeResultSet.next()) {
                double totalSize = sizeResultSet.getDouble("totalSize");
                result[1] = totalSize;
            }
        }

        return result;
    }
}
