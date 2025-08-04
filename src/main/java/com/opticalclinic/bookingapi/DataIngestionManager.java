package com.opticalclinic.bookingapi;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DataIngestionManager {

    private Connection dbConnection;

    public DataIngestionManager(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<User> ingestAllUserData(String csvFilePath) {
        List<User> users = new ArrayList<>();

        // Ingest from CSV
        List<User> csvUsers = CsvUserDataExtractor.readUsersFromCsv(csvFilePath);
        users.addAll(csvUsers);

        // Ingest from DB
        DatabaseUserDataExtractor dbExtractor = new DatabaseUserDataExtractor(dbConnection);
        List<User> dbUsers = dbExtractor.extractUsers();
        users.addAll(dbUsers);

        return users;
    }
}
