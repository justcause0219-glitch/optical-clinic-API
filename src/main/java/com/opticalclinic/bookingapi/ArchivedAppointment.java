package com.opticalclinic.bookingapi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArchivedAppointment extends OpticalClinicBookingSystem {
    private int appointmentID;
    private String userID;
    private String date;
    private String time;
    private String doctor;
    private String status;
    private String reason;

public ArchivedAppointment(int appointmentID, String userID, String date, String time, String doctor, String status, String reason) {
    this.appointmentID = appointmentID;
        this.userID = userID;
        this.date = date;
        this.time = time;
        this.doctor = doctor;
        this.status = status;
        this.reason = reason;
}
public ArchivedAppointment(String reason) {
    this.reason = reason;
}

public ArchivedAppointment() {
}

// Getters and Setters
public int getAppointmentID() {
    return appointmentID;
}

public void setAppointmentID(int appointmentID) {
    this.appointmentID = appointmentID;
}

public String getUserID() {
    return userID;
}

public void setUserID(String userID) {
    this.userID = userID;
}

public String getDate() {
    return date;
}

public void setDate(String date) {
    this.date = date;
}

public String getTime() {
    return time;
}

public void setTime(String time) {
    this.time = time;
}

public String getDoctor() {
    return doctor;
}

public void setDoctor(String doctor) {
    this.doctor = doctor;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public String getReason() {
    return reason;
}

public void setReason(String reason) {
    this.reason = reason;
}

    protected void viewArchivedAppointments() {
        JFrame archivedFrame = new JFrame("Archived Appointments");
        archivedFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        archivedFrame.setVisible(true);

        archivedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        archivedFrame.setLocationRelativeTo(null);
    
        // Column names for the archived table
        String[] columnNames = {"Name", "Date", "Time", "Doctor", "Reason"};
    
        // Create the DefaultTableModel for archived appointments
        DefaultTableModel archivedModel = new DefaultTableModel(columnNames, 0);
    
        // Fetch archived appointments from the database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
            String query = "SELECT Username, AppointmentDate, Time, Doctor, CancellationReason FROM archived_appointments";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
    
                // Populate the table model with data from the ResultSet
                while (rs.next()) {
                    String name = rs.getString("Username");
                    String date = rs.getString("AppointmentDate");
                    String time = rs.getString("Time");
                    String doctor = rs.getString("Doctor");
                    String reason = rs.getString("CancellationReason");
    
                    // Add row to the table model
                    archivedModel.addRow(new Object[]{name, date, time, doctor, reason});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(archivedFrame, "Error retrieving archived appointments: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    
        // Create the JTable with the archived model
        JTable archivedTable = new JTable(archivedModel);
        JScrollPane scrollPane = new JScrollPane(archivedTable);
    
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
    
        // Exit Button (Red) at the bottom
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(255, 69, 58));  // Red background
        exitButton.setForeground(Color.WHITE);  // White text color
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));  // Smaller font size
        exitButton.setFocusPainted(false);  // Removes focus border
        exitButton.setPreferredSize(new Dimension(150, 30));  // Smaller button size
        exitButton.setFocusable(false);
        exitButton.addActionListener(e -> archivedFrame.dispose());
        ad.menu();  // Close the frame
    
        // Create a panel for the exit button at the bottom
        JPanel exitButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        exitButtonPanel.setOpaque(false);  // Transparent background
        exitButtonPanel.add(exitButton);
    
        // Add the exit button panel to the bottom of the main panel
        panel.add(exitButtonPanel, BorderLayout.SOUTH);
    
        // Add the main panel to the frame
        archivedFrame.add(panel);
        archivedFrame.setVisible(true);
    }

    public void archiveAppointment(String[] appointment) {
        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement idStmt = null;
        PreparedStatement deleteStmt = null;
        ResultSet rs = null;
    
        try {
            // Open database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35");
            conn.setAutoCommit(false); // Start transaction
    
            // Step 1: Archive the appointment by inserting into the archived_appointments table
            String insertQuery = "INSERT INTO archived_appointments (Username, AppointmentDate, Time, Doctor, CancellationReason) VALUES (?, ?, ?, ?, ?)";
            insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, appointment[0]);  // Username
            insertStmt.setString(2, appointment[3]);  // AppointmentDate
            insertStmt.setString(3, appointment[4]);  // Time
            insertStmt.setString(4, appointment[5]);  // Doctor
            insertStmt.setString(5, appointment[6]);  // CancellationReason
            insertStmt.executeUpdate();
    
            // Step 2: Retrieve TimeSlotID and DoctorID from the database
            String retrieveIdsQuery = "SELECT ts.TimeSlotID, d.DoctorID " +
                                      "FROM timeslots ts " +
                                      "JOIN doctors d ON d.Name = ? " +
                                      "WHERE ts.Time = ?";
            idStmt = conn.prepareStatement(retrieveIdsQuery);
            idStmt.setString(1, appointment[5]);  // Doctor name
            idStmt.setString(2, appointment[4]);  // Time
            rs = idStmt.executeQuery();
    
            if (rs.next()) {
                int timeSlotId = rs.getInt("TimeSlotID");
                int doctorId = rs.getInt("DoctorID");
    
                // Step 3: Delete the original appointment using retrieved IDs
                String deleteQuery = "DELETE FROM appointments WHERE AppointmentDate = ? AND TimeSlotID = ? AND DoctorID = ?";
                deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setString(1, appointment[3]);  // AppointmentDate
                deleteStmt.setInt(2, timeSlotId);         // TimeSlotID
                deleteStmt.setInt(3, doctorId);           // DoctorID
    
                int affectedRows = deleteStmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(null, "Appointment successfully archived and removed from the active schedule.");
                } else {
                    conn.rollback(); // Rollback transaction
                    JOptionPane.showMessageDialog(null, "No matching appointment found to remove.");
                }
            } else {
                conn.rollback(); // Rollback transaction
                JOptionPane.showMessageDialog(null, "TimeSlotID or DoctorID not found. Cannot delete appointment.");
            }
        } catch (SQLException ex) {
            // Handle SQL exceptions
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, "Error archiving the appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (insertStmt != null) insertStmt.close();
                if (idStmt != null) idStmt.close();
                if (deleteStmt != null) deleteStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    
}