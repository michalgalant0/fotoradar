package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Segment;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class StatusOperations {
    private Connection connection;

    public StatusOperations(Connection connection) {
        this.connection = connection;
    }
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


}
