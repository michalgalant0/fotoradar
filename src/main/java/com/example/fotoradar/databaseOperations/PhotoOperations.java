package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Photo;
import com.example.fotoradar.models.Thumbnail;

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
        String query = "INSERT INTO Photo (file_name, version_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, photo.getFileName());
            preparedStatement.setInt(2, photo.getParentId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
