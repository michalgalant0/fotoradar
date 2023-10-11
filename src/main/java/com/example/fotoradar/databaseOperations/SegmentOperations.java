package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Segment;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SegmentOperations {
    private final Connection connection;

    public SegmentOperations(Connection connection) {
        this.connection = connection;
    }

    // Pobieranie wszystkich obiektów z bazy
    public List<Segment> getAllSegments(int parentCollectibleId) throws SQLException {
        List<Segment> segments = new ArrayList<>();
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
                    int statusId = resultSet.getInt("status_id");

                    Segment segment = new Segment(id, title, startDate, finishDate, description, statusId, parentCollectibleId);
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

                    return new Segment(id, title, startDate, finishDate, description, statusId, parentCollectibleId);
                }
            }
        }
        return null; // Obiekt nie został znaleziony
    }

    // Dodawanie obiektu do bazy
    public boolean addSegment(Segment segment) throws SQLException {
        String query = "INSERT INTO Segment (title, start_datetime, finish_datetime, description, status_id, collectible_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, segment.getTitle());
            preparedStatement.setString(2, segment.getStartDate());
            preparedStatement.setString(3, segment.getFinishDate());
            preparedStatement.setString(4, segment.getDescription());
            preparedStatement.setInt(5, segment.getStatusId());
            preparedStatement.setInt(6, segment.getParentCollectibleId());

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
        String query = "UPDATE Segment SET title=?, start_datetime=?, finish_datetime=?, description=?, status_id=? WHERE segment_id=? AND collectible_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, segment.getTitle());
            preparedStatement.setString(2, segment.getStartDate());
            preparedStatement.setString(3, segment.getFinishDate());
            preparedStatement.setString(4, segment.getDescription());

            preparedStatement.setInt(5, segment.getStatusId());
            preparedStatement.setInt(6, segment.getId());
            preparedStatement.setInt(7, segment.getParentCollectibleId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }


}
