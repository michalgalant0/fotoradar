package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Thumbnail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ThumbnailOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public ThumbnailOperations() throws SQLException {
    }

    // Pobieranie wszystkich miniatur z bazy dla danego obiektu
    public ArrayList<Thumbnail> getAllThumbnails(int parentCollectibleId) throws SQLException {
        ArrayList<Thumbnail> thumbnails = new ArrayList<>();
        String query = "SELECT * FROM Thumbnail WHERE collectible_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, parentCollectibleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("thumbnail_id");
                    String fileName = resultSet.getString("file_name");

                    Thumbnail thumbnail = new Thumbnail(id, fileName, parentCollectibleId);
                    thumbnails.add(thumbnail);
                }
            }
        }
        return thumbnails;
    }


    // Pobieranie jednej miniatury z bazy po ID
    public Thumbnail getThumbnailById(int id) throws SQLException {
        String query = "SELECT * FROM Thumbnail WHERE thumbnail_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String fileName = resultSet.getString("file_name");
                    int parentCollectibleId = resultSet.getInt("collectible_id");

                    return new Thumbnail(id, fileName, parentCollectibleId);
                }
            }
        }
        return null; // miniatura nie została znaleziona
    }

    // Dodawanie miniatury do bazy
    public boolean addThumbnail(Thumbnail thumbnail) throws SQLException {
        String query = "INSERT INTO Thumbnail (file_name, collectible_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, thumbnail.getFileName());
            preparedStatement.setInt(2, thumbnail.getParentId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Usuwanie miniatury z bazy po ID
    public boolean deleteThumbnail(int id) throws SQLException {
        String query = "DELETE FROM Thumbnail WHERE thumbnail_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Aktualizacja istniejącej miniatury w bazie
    public boolean updateThumbnail(Thumbnail thumbnail) throws SQLException {
        String query = "UPDATE Thumbnail SET file_name=?, collectible_id=? WHERE thumbnail_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, thumbnail.getFileName());
            preparedStatement.setInt(2, thumbnail.getParentId());

            preparedStatement.setInt(3, thumbnail.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public int getThumbnailId(String fileName, int parentCollectibleId) {
        String query = "SELECT thumbnail_id FROM Thumbnail WHERE file_name=? AND collectible_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, fileName);
            preparedStatement.setInt(2, parentCollectibleId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getInt("thumbnail_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
