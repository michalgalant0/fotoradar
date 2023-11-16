package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Version;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VersionOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public VersionOperations() throws SQLException {}

    // Pobieranie wszystkich wersji z bazy
    public ArrayList<Version> getAllVersions(int parentSegmentId) throws SQLException {
        ArrayList<Version> versions = new ArrayList<>();
        String query = "SELECT * FROM Version WHERE segment_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, parentSegmentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("version_id");
                    String name = resultSet.getString("name");
                    String startDate = resultSet.getString("start_datetime");
                    String finishDate = resultSet.getString("finish_datetime");
                    String description = resultSet.getString("description");
                    int teamId = resultSet.getInt("team_id");

                    Version version = new Version(id, name, startDate, finishDate, description, teamId, parentSegmentId);
                    versions.add(version);
                }
            }
        }
        return versions;
    }

    // Pobieranie jednego obiektu z bazy po ID
    public Version getVersionById(int id, int parentSegmentId, int teamId) throws SQLException {
        String query = "SELECT * FROM Version WHERE (version_id=? AND team_id=? AND segment_id=?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(6, parentSegmentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String title = resultSet.getString("name");
                    String startDate = resultSet.getString("start_datetime");
                    String finishDate = resultSet.getString("finish_datetime");
                    String description = resultSet.getString("description");


                    return new Version(id, title, startDate, finishDate, description, teamId, parentSegmentId );
                }
            }
        }
        return null; // Obiekt nie został znaleziony
    }

    // Dodawanie obiektu do bazy
    public boolean addVersion(Version version) throws SQLException {
        String query = "INSERT INTO Version (name, start_datetime, finish_datetime, description, team_id, segment_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, version.getName());
            preparedStatement.setString(2, version.getStartDate());
            preparedStatement.setString(3, version.getFinishDate());
            preparedStatement.setString(4, version.getDescription());
            preparedStatement.setInt(5, version.getTeamId());
            preparedStatement.setInt(6, version.getParentSegmentId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Usuwanie obiektu z bazy po ID
    public boolean deleteVersion(int id) throws SQLException {
        String query = "DELETE FROM Version WHERE version_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Aktualizacja istniejącego obiektu w bazie
    public boolean updateVersion(Version version) throws SQLException {
        String query = "UPDATE Version SET name=?, start_datetime=?, finish_datetime=?, description=?, team_id=? WHERE version_id=? AND segment_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, version.getName());
            preparedStatement.setString(2, version.getStartDate());
            preparedStatement.setString(3, version.getFinishDate());
            preparedStatement.setString(4, version.getDescription());
            preparedStatement.setInt(5, version.getTeamId());
            preparedStatement.setInt(6,version.getId());
            preparedStatement.setInt(7, version.getParentSegmentId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteVersionWithSubstructures(int versionId) throws SQLException {
        // Usuwanie wszystkich podrędnych zdjęć
        new PhotoOperations().deletePhotosByVersionId(versionId);

        // Usuwanie wersji
        String query = "DELETE FROM VERSION WHERE version_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, versionId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteVersionsByCollectionId(int collectionId) throws SQLException {
        String query = "DELETE FROM VERSION WHERE segment_id IN (SELECT segment_id FROM SEGMENT WHERE collectible_id IN (SELECT collectible_id FROM COLLECTIBLE WHERE collection_id = ?))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectionId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteVersionsByCollectibleId(int collectibleId) throws SQLException {
        String query = "DELETE FROM VERSION WHERE segment_id IN (SELECT segment_id FROM SEGMENT WHERE collectible_id = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectibleId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteVersionsByThumbnailId(int thumbnailId) throws SQLException {
        String query = "DELETE FROM VERSION WHERE segment_id IN (SELECT segment_id FROM SEGMENT WHERE thumbnail_id = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, thumbnailId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteVersionsBySegmentId(int segmentId) throws SQLException {
        String query = "DELETE FROM VERSION WHERE segment_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, segmentId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
