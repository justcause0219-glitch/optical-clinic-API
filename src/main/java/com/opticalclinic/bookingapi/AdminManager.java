package com.opticalclinic.bookingapi;

import java.sql.*;

public class AdminManager {
    public Admin authenticateAdmin(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
            String query = "SELECT * FROM admin WHERE Username = admin AND Password = ? AND Role = 'Admin'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("FullName");
                String phone = rs.getString("PhoneNumber");
                String adminID = rs.getString("EmployeeID");

                return new Admin(fullName, phone, 0, adminID); // pass 0 if you're not using int ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

