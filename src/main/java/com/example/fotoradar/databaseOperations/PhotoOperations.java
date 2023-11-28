package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PhotoOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public PhotoOperations() throws SQLException {
    }

    // Pobieranie wszystkich zdjęć z bazy dla danej wersji
    public ArrayList<Photo> getAllPhotos(int parentVersionId) throws SQLException {
        ArrayList<Photo> photos = new ArrayList<>();
        String query = "SELECT * FROM Photo WHERE version_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, parentVersionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("photo_id");
                    String fileName = resultSet.getString("file_name");

                    Photo photo = new Photo(id, fileName, parentVersionId);
                    photos.add(photo);
                }
            }
        }
        return photos;
    }

    // Dodawanie zdjęcia do bazy
    public boolean addPhoto(Photo photo) throws SQLException {
        String query = "INSERT INTO Photo (file_name, file_size, version_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, photo.getFileName());
            preparedStatement.setFloat(2, photo.getFileSize());
            preparedStatement.setInt(3, photo.getParentId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deletePhoto(int id) throws SQLException {
        String query = "DELETE FROM Photo WHERE photo_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deletePhotosByCollectionId(int collectionId) throws SQLException {
        String query = "DELETE FROM PHOTO WHERE version_id IN (SELECT version_id FROM VERSION WHERE segment_id IN (SELECT segment_id FROM SEGMENT WHERE collectible_id IN (SELECT collectible_id FROM COLLECTIBLE WHERE collection_id = ?)))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectionId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deletePhotosByCollectibleId(int collectibleId) throws SQLException {
        String query = "DELETE FROM PHOTO WHERE version_id IN (SELECT version_id FROM VERSION WHERE segment_id IN (SELECT segment_id FROM SEGMENT WHERE collectible_id = ?))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, collectibleId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deletePhotosBySegmentId(int segmentId) throws SQLException {
        String query = "DELETE FROM PHOTO WHERE version_id IN (SELECT version_id FROM VERSION WHERE segment_id = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, segmentId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deletePhotosByVersionId(int versionId) throws SQLException {
        String query = "DELETE FROM PHOTO WHERE version_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, versionId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deletePhotosByThumbnailId(int thumbnailId) throws SQLException {
        String query = "DELETE FROM PHOTO WHERE version_id IN (SELECT version_id FROM VERSION WHERE segment_id IN (SELECT segment_id FROM SEGMENT WHERE thumbnail_id = ?))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, thumbnailId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
