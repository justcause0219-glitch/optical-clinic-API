package com.opticalclinic.bookingapi;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUserDataExtractor {

    private Connection connection;

    public DatabaseUserDataExtractor(Connection connection) {
        this.connection = connection;
    }

    public List<User> extractUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT username, password, full_name, phone_number, birthdate FROM users"; // Adjust table/column names if needed

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullName = rs.getString("full_name");
                String phoneNumber = rs.getString("phone_number");
                String birthdate = rs.getString("birthdate");

                User user = new User(fullName, phoneNumber, username, password, birthdate);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}

