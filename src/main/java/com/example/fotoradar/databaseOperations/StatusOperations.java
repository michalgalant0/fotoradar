package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class StatusOperations {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public StatusOperations() throws SQLException {
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
}
