package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamOperations {
    private final Connection connection;

    public TeamOperations(Connection connection) {
        this.connection = connection;
    }

    public List<Team> getAllTeams() throws SQLException {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM Team";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("team_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int parentCollectionId = resultSet.getInt("collection_id");

                Team team = new Team(id, name, description, parentCollectionId);
                teams.add(team);
            }
        }
        return teams;
    }

    public List<Team> getAllCollectionTeams(int parentCollectionId) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM Team WHERE collection_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, parentCollectionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("team_id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    Team team = new Team(id, name, description, parentCollectionId);
                    teams.add(team);
                }
            }
        }
        return teams;
    }

    public Team getTeamById(int id) throws SQLException {
        String query = "SELECT * FROM Team WHERE team_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int parentCollectionId = resultSet.getInt("collection_id");

                    return new Team(id, name, description, parentCollectionId);
                }
            }
        }
        return null;
    }

    // Dodawanie zespołu do bazy
    public boolean addTeam(Team team) throws SQLException {
        String query = "INSERT INTO Team (name, description, collection_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, team.getName());
            preparedStatement.setString(2, team.getDescription());
            preparedStatement.setInt(3, team.getParentCollectionId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Usuwanie zespołu z bazy po ID
    public boolean deleteTeam(int id) throws SQLException {
        String query = "DELETE FROM Team WHERE team_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Aktualizacja istniejącego zespołu w bazie
    public boolean updateTeam(Team team) throws SQLException {
        String query = "UPDATE Team SET name=?, description=?, collection_id=? WHERE team_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, team.getName());
            preparedStatement.setString(2, team.getDescription());
            preparedStatement.setInt(3, team.getParentCollectionId());
            preparedStatement.setInt(4, team.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
