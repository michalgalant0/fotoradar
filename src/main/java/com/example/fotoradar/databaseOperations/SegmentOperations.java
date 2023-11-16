package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Segment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SegmentOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public SegmentOperations() throws SQLException {

    }

    // Pobieranie wszystkich obiektów z bazy
    public ArrayList<Segment> getAllSegments(int parentCollectibleId) throws SQLException {
        ArrayList<Segment> segments = new ArrayList<>();
        String query = "SELECT * FROM Segment WHERE collectible_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, parentCollectibleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("segment_id");
                    String title = resultSet.getString("title");
                    String startDate = resultSet.getString("start_datetime");
                    String finishDate = resultSet.getString("finish_datetime");
                    String description = resultSet.getString("description");
                    String coords = resultSet.getString("coords");
                    int statusId = resultSet.getInt("status_id");
                    int thumbnailId = resultSet.getInt("thumbnail_id");

                    Segment segment = new Segment(id, title, startDate, finishDate, description, coords, statusId, parentCollectibleId, thumbnailId);
                    segments.add(segment);
                }
            }
        }
        return segments;
    }

    // Pobieranie wszystkich obiektów z bazy
    public ArrayList<Segment> getAllSegmentsForThumbnail(int parentThumbnailId) throws SQLException {
        ArrayList<Segment> segments = new ArrayList<>();
        String query = "SELECT * FROM Segment WHERE thumbnail_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, parentThumbnailId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("segment_id");
                    String title = resultSet.getString("title");
                    String startDate = resultSet.getString("start_datetime");
                    String finishDate = resultSet.getString("finish_datetime");
                    String description = resultSet.getString("description");
                    String coords = resultSet.getString("coords");
                    int statusId = resultSet.getInt("status_id");
                    int parentCollectibleId = resultSet.getInt("thumbnail_id");

                    Segment segment = new Segment(id, title, startDate, finishDate, description, coords, statusId, parentCollectibleId, parentThumbnailId);
                    segments.add(segment);
                }
            }
        }
        return segments;
    }

    // Pobieranie jednego obiektu z bazy po ID
    public Segment getSegmentById(int id, int statusId, int parentCollectibleId) throws SQLException {
        String query = "SELECT * FROM Segment WHERE (segment_id=? AND status_id=? AND collectible_id=?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, statusId);
            preparedStatement.setInt(3, parentCollectibleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String startDate = resultSet.getString("start_datetime");
                    String finishDate = resultSet.getString("finish_datetime");
                    String description = resultSet.getString("description");
                    String coords = resultSet.getString("coords");
                    int thumbnailId = resultSet.getInt("thumbnail_id");

                    return new Segment(id, title, startDate, finishDate, description, coords, statusId, parentCollectibleId, thumbnailId);
                }
            }
        }
        return null; // Obiekt nie został znaleziony
    }

    // Dodawanie obiektu do bazy
    public boolean addSegment(Segment segment) throws SQLException {
        String query = "INSERT INTO Segment (title, start_datetime, finish_datetime, description, coords, status_id, collectible_id, thumbnail_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, segment.getTitle());
            preparedStatement.setString(2, segment.getStartDate());
            preparedStatement.setString(3, segment.getFinishDate());
            preparedStatement.setString(4, segment.getDescription());
            preparedStatement.setString(5, segment.getCoords());
            preparedStatement.setInt(6, segment.getStatusId());
            preparedStatement.setInt(7, segment.getParentCollectibleId());
            preparedStatement.setInt(8, segment.getThumbnailId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Usuwanie obiektu z bazy po ID
    public boolean deleteSegment(int id) throws SQLException {
        String query = "DELETE FROM Segment WHERE segment_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Aktualizacja istniejącego obiektu w bazie
    public boolean updateSegment(Segment segment) throws SQLException {
        String query = "UPDATE Segment SET title=?, start_datetime=?, finish_datetime=?, description=?, status_id=? WHERE segment_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, segment.getTitle());
            preparedStatement.setString(2, segment.getStartDate());
            preparedStatement.setString(3, segment.getFinishDate());
            preparedStatement.setString(4, segment.getDescription());

            preparedStatement.setInt(5, segment.getStatusId());
            preparedStatement.setInt(6, segment.getId());

            System.out.println(preparedStatement);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteSegmentWithSubstructures(int segmentId) throws SQLException {
        // Usuwanie wszystkich podrędnych wersji
        new VersionOperations().deleteVersionsBySegmentId(segmentId);

        // Usuwanie wszystkich podrędnych zdjęć
        new PhotoOperations().deletePhotosBySegmentId(segmentId);

        // Usuwanie segmentu
        String query = "DELETE FROM SEGMENT WHERE segment_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, segmentId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteSegmentsByCollectionId(int collectionId) throws SQLException {
        String query = "DELETE FROM SEGMENT WHERE collectible_id IN (SELECT collectible_id FROM COLLECTIBLE WHERE collection_id = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectionId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteSegmentsByCollectibleId(int collectibleId) throws SQLException {
        String query = "DELETE FROM SEGMENT WHERE collectible_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectibleId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteSegmentsByThumbnailId(int thumbnailId) throws SQLException {
        String query = "DELETE FROM SEGMENT WHERE thumbnail_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, thumbnailId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
