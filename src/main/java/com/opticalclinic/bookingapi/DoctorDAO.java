package com.opticalclinic.bookingapi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorDAO {
    public static Doctor login(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
            String query = "SELECT DoctorID, Name, Specialty FROM doctors WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String doctorID = rs.getString("DoctorID");
                String fullName = rs.getString("Name");
                String specialty = rs.getString("Specialty");

                // Placeholder for phone and ID (not in DB yet)
                return new Doctor(fullName, "N/A", 0, doctorID, specialty);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
