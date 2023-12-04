package com.example.fotoradar.databaseOperations;

import com.example.fotoradar.models.Status;
import lombok.Getter;

import java.sql.SQLException;
import java.util.ArrayList;

@Getter
public class StatusManager {
    private static StatusManager instance;
    private ArrayList<Status> statuses;

    private StatusManager() {
        try {
            fetchStatusesFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static StatusManager getInstance() {
        if (instance == null) {
            instance = new StatusManager();
        }
        return instance;
    }

    // Metoda do pobierania statusów z bazy danych
    public void fetchStatusesFromDatabase() throws SQLException {
        statuses = new StatusOperations().getAllStatuses();
    }
}
