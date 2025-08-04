package com.opticalclinic.bookingapi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public String getPhoneNumberById(int userId) {
        String phoneNumber = null;

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/project", "root", "Jessa@35"
            );

            String query = "SELECT PhoneNumber FROM users WHERE UserId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                phoneNumber = rs.getString("PhoneNumber");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return phoneNumber;
    }
}

