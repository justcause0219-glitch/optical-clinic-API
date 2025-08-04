package com.opticalclinic.bookingapi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends OpticalClinicBookingSystem {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Jessa@35";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    public List<String> getDoctors() {
        List<String> doctors = new ArrayList<>();
        String query = "SELECT Name FROM doctors"; 
        try (Connection connection = getConnection(); java.sql.Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                doctors.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public boolean isTimeSlotAvailable(int doctorID, String date, String time) {
        String query = "SELECT COUNT(*) FROM appointments WHERE DoctorID = ? AND AppointmentDate = ? AND TimeSlotID = ?";
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorID);
            stmt.setString(2, date);
            stmt.setString(3, time);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // If no appointments found, the slot is available
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getDoctorID(String doctorName) {
        String query = "SELECT DoctorID FROM doctors WHERE Name = ?";
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, doctorName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("DoctorID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if doctor is not found
    }

    public int getTimeSlotID(String time) {
        String query = "SELECT TimeSlotID FROM timeslots WHERE Time = ?";
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, time);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("TimeSlotID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if time slot is not found
    }

    public boolean insertUserAndAppointment(String name, int doctorID, int timeSlotID, String date) {
        try (Connection connection = getConnection()) {
            // Insert user
            String userQuery = "INSERT INTO users (Username) VALUES (?)";
            try (PreparedStatement userStmt = connection.prepareStatement(userQuery, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, name);
                int affectedRows = userStmt.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("No rows affected in user insertion.");
                    return false;
                }
                
                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userID = generatedKeys.getInt(1);
                    System.out.println("Generated UserID: " + userID);
                    
                    // Insert appointment
                    String appointmentQuery = "INSERT INTO appointments (UserID, DoctorID, TimeSlotID, AppointmentDate) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement appointmentStmt = connection.prepareStatement(appointmentQuery)) {
                        appointmentStmt.setInt(1, userID);
                        appointmentStmt.setInt(2, doctorID);
                        appointmentStmt.setInt(3, timeSlotID);
                        appointmentStmt.setString(4, date);
                        affectedRows = appointmentStmt.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Appointment booking successful.");
                            return true;
                        } else {
                            System.out.println("Failed to insert appointment.");
                        }
                    }
                } else {
                    System.out.println("Failed to generate UserID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    
    
    }
    public void register(){
        
    }

    public static String getPatientNameById(Connection conn, int patientId) {
        String name = null;
        try {
            String query = "SELECT name FROM patients WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                name = rs.getString("name");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(); // or log properly
        }
        return name;
    }

    public static String getDoctorNameById(Connection conn, int doctorId) {
        String name = null;
        try {
            String query = "SELECT name FROM doctors WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                name = rs.getString("name");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
   
}
