package com.opticalclinic.bookingapi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class UserManager extends OpticalClinicBookingSystem {

    public void initializeAdmin() throws SQLException {
        Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        String checkAdminQuery = "SELECT COUNT(*) FROM Users WHERE username = 'admin'";
        PreparedStatement checkStmt = conn.prepareStatement(checkAdminQuery);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            String insertAdminQuery = "INSERT INTO Users (Username, Password, FullName, PhoneNumber, BirthDate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertAdminQuery);
            insertStmt.setString(1, "admin");
            insertStmt.setString(2, "admin123");
            insertStmt.setString(3, "Jessa Melargo");
            insertStmt.setString(4, "09009878987");
            insertStmt.setString(5, "2005-01-01");
            insertStmt.executeUpdate();
            System.out.println("Admin user created with username 'admin' and default password 'admin123'");
        }

        rs.close();
        checkStmt.close();
        conn.close();
    }

    public User authenticateUser(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getString("FullName"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("BirthDate")
                );
                return user;
            }
        }
        return null;
    }

    public Admin authenticateAdmin(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE Username = ? AND Password = ? AND Username = 'admin'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Admin(
                    rs.getString("FullName"),
                    rs.getString("PhoneNumber"),
                    0,
                    "admin"
                );
            }
        }
        return null;
    }

    public Doctor authenticateDoctor(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM doctors WHERE Username = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Doctor(
                    rs.getString("Name"),
                    "N/A",
                    0,
                    rs.getString("DoctorID"),
                    rs.getString("Specialty")
                );
            }
        }
        return null;
    }

    public boolean registerUser(String username, String password, String fullName, String phoneNumber, String birthdate) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String insertQuery = "INSERT INTO Users (Username, Password, FullName, PhoneNumber, BirthDate) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, fullName);
                statement.setString(4, phoneNumber);
                statement.setString(5, birthdate);
                return statement.executeUpdate() > 0;
            }
        }
    }

    public boolean isValidPhilippineNumber(String phoneNumber) {
        return phoneNumber.matches("^09[0-9]{9}$");
    }

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$");
    }

    // Registration, cancellation and other methods remain unchanged
    // ...

    protected void handleRegistration(JFrame frame, JTextField usernameField, JPasswordField passwordField, 
                                 JPasswordField confirmPasswordField, JTextField nameField, 
                                 JTextField phoneField, JComboBox<String> yearComboBox, 
                                 JComboBox<String> monthComboBox, JComboBox<String> dayComboBox, 
                                 Employee employee) {

    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());
    String confirmPassword = new String(confirmPasswordField.getPassword());
    String fullName = nameField.getText();
    String phoneNumber = phoneField.getText();
    String birthdate = yearComboBox.getSelectedItem() + "-" +
            monthComboBox.getSelectedItem() + "-" +
            dayComboBox.getSelectedItem();
    
    // Check if any field is empty
    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
        fullName.isEmpty() || phoneNumber.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Registration Error", JOptionPane.ERROR_MESSAGE);
    } 
    // Check if passwords match
    else if (!password.equals(confirmPassword)) {
        JOptionPane.showMessageDialog(frame, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
    } 
    // Check if the phone number is valid
    else if (!um.isValidPhilippineNumber(phoneNumber)) {
        JOptionPane.showMessageDialog(frame, "Invalid Philippine phone number.", "Registration Error", JOptionPane.ERROR_MESSAGE);
    } 
    // Check if password is valid
    else if (!um.isValidPassword(password)) {
        JOptionPane.showMessageDialog(frame, "Password must contain at least one uppercase letter, one lowercase letter, and one special character.", "Registration Error", JOptionPane.ERROR_MESSAGE);
    } 
    else {
        // Proceed to register user
        try {
            if (um.registerUser(username, password, fullName, phoneNumber, birthdate)) {
                JOptionPane.showMessageDialog(frame, "Registration Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                ad.menu();  
                frame.dispose();  // Close the registration window
            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed, please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
 public boolean cancelAppointment(
        JTable table,
        int selectedRow,
        String reason
    ) {
        String fullName = (String) table.getValueAt(selectedRow, 0);
        String age = (String) table.getValueAt(selectedRow, 1);
        String contact = (String) table.getValueAt(selectedRow, 2);
        String date = (String) table.getValueAt(selectedRow, 3);
        String time = (String) table.getValueAt(selectedRow, 4);
        String doctor = (String) table.getValueAt(selectedRow, 5);

        int userId = -1;
        int appointmentId = -1;

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {

            // Get UserID
            String userQuery = "SELECT UserID FROM users WHERE FullName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(userQuery)) {
                stmt.setString(1, fullName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("UserID");
                } else {
                    JOptionPane.showMessageDialog(null, "User not found.");
                    return false;
                }
            }

            // Get AppointmentID
            String apptQuery = "SELECT AppointmentID FROM appointments WHERE UserID = ? AND Date = ? AND Time = ? AND Doctor = ?";
            try (PreparedStatement stmt = conn.prepareStatement(apptQuery)) {
                stmt.setInt(1, userId);
                stmt.setString(2, date);
                stmt.setString(3, time);
                stmt.setString(4, doctor);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    appointmentId = rs.getInt("AppointmentID");
                } else {
                    JOptionPane.showMessageDialog(null, "Appointment not found.");
                    return false;
                }
            }

            // Insert into cancellations table
            String cancelSQL = "INSERT INTO cancellations (UserID, AppointmentID, Reason) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(cancelSQL)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, appointmentId);
                stmt.setString(3, reason);
                stmt.executeUpdate();
            }

            // Delete from appointments table
            String deleteSQL = "DELETE FROM appointments WHERE AppointmentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {
                stmt.setInt(1, appointmentId);
                stmt.executeUpdate();
            }

            // Archive the appointment
            String[] canceledAppointment = {fullName, age, contact, date, time, doctor, reason};
            ArchivedAppointment archive = new ArchivedAppointment();
            archive.archiveAppointment(canceledAppointment);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error during cancellation.");
            return false;
        }
    }
protected void handleRegistrationUser(JFrame frame, JTextField usernameField, JPasswordField passwordField, 
                                      JPasswordField confirmPasswordField, JTextField nameField, 
                                      JTextField phoneField, JComboBox<String> yearComboBox, 
                                      JComboBox<String> monthComboBox, JComboBox<String> dayComboBox, 
                                      Client client) {

    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());
    String confirmPassword = new String(confirmPasswordField.getPassword());
    String fullName = nameField.getText();
    String phoneNumber = phoneField.getText();
    String birthdate = yearComboBox.getSelectedItem() + "-" +
                       monthComboBox.getSelectedItem() + "-" +
                       dayComboBox.getSelectedItem();

    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
        fullName.isEmpty() || phoneNumber.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "All fields must be filled out", "Registration Error", JOptionPane.ERROR_MESSAGE);
    } else if (!password.equals(confirmPassword)) {
        JOptionPane.showMessageDialog(frame, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
    } else if (!um.isValidPhilippineNumber(phoneNumber)) {
        JOptionPane.showMessageDialog(frame, "Invalid Philippine phone number.", "Registration Error", JOptionPane.ERROR_MESSAGE);
    } else if (!um.isValidPassword(password)) {
        JOptionPane.showMessageDialog(frame, "Password must contain at least one uppercase letter, one lowercase letter, and one special character.", "Registration Error", JOptionPane.ERROR_MESSAGE);
    } else {
        try {
            if (um.registerUser(username, password, fullName, phoneNumber, birthdate)) {
                // ✅ Only create User object and write to CSV after successful DB registration
                User user = new User(fullName, phoneNumber, username, password, birthdate);
                CsvUserWriter.writeUserToCsv(user, "users.csv");
                List<User> users = new ArrayList<>();
users.add(user);
XmlUserWriter.writeUsersToXml(users, "registered_users.xml");
                JOptionPane.showMessageDialog(frame, "Registration Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                us.menu();  
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed, please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

}
