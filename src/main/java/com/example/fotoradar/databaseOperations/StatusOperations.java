package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class StatusOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public StatusOperations() throws SQLException {
    }

    // todo do usunięcia - dodać statusy na sztywno w bazie
    public void addStatusValues() throws SQLException {
        String[] statusValues = {"notStarted", "started", "notEnded", "ended"};
        for (String status : statusValues) {
            String query = "INSERT INTO Status (name) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, status);
                preparedStatement.executeUpdate();
            }
        }
    }

    public ArrayList<Status> getAllStatuses() throws SQLException {
        ArrayList<Status> statuses = new ArrayList<>();
        String query = "SELECT * FROM Status";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("status_id");
                String name = resultSet.getString("name");

                Status status = new Status(id, name);
                statuses.add(status);
            }
        }
        return statuses;
    }

    public Status getStatusById(int id) throws SQLException {
        String query = "SELECT * FROM Status WHERE status_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");

                    return new Status(id, name);
                }
            }
        }
        return null;
    }

}
