package com.opticalclinic.bookingapi;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;

public class UserViewAppointment {
    private static DefaultTableModel model;
    
 
    protected void viewAppointmentsUser() {
        JFrame appointmentFrame = new JFrame("View Appointments (Admin)");
        appointmentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        appointmentFrame.setVisible(true);

        appointmentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        appointmentFrame.setLocationRelativeTo(null);
    
        // Column names for the table
        String[] columnNames = {"Name", "Age", "Contact", "Date", "Time", "Doctor"};
    
        // Create the DefaultTableModel with the column names
        model = new DefaultTableModel(columnNames, 0);
    
        // Retrieve the appointment data from the database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
            String query = "SELECT u.FullName, u.Age, u.PhoneNumber, a.AppointmentDate, ts.Time, d.Name AS Doctor " +
                           "FROM appointments a " +
                           "JOIN users u ON a.UserID = u.UserID " +
                           "JOIN doctors d ON a.DoctorID = d.DoctorID " +
                           "JOIN timeslots ts ON a.TimeSlotID = ts.TimeSlotID";
    
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("FullName");
                    String age = rs.getString("Age");
                    String contact = rs.getString("PhoneNumber");
                    String date = rs.getString("AppointmentDate");
                    String time = rs.getString("Time");
                    String doctor = rs.getString("Doctor");
    
                    model.addRow(new Object[]{name, age, contact, date, time, doctor});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(appointmentFrame, "Error retrieving appointment data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    
        // Create the JTable with the model
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
    
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
    
         JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(255, 69, 0));  // Red background (for cancel)
        cancelButton.setForeground(Color.WHITE);  // White text color
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));  // Smaller font size
        cancelButton.setFocusPainted(false);  // Removes focus border
        cancelButton.setPreferredSize(new Dimension(150, 30));  // Smaller button size
        cancelButton.setEnabled(true);  // Enable the button
        cancelButton.setFocusable(false);
        
        
        cancelButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();  // Get the selected row index
        
            // Check if a row is selected
            if (selectedRow != -1) {
                // Ask the user for the reason using a dialog box
                String reason = JOptionPane.showInputDialog(null, "Please provide a reason for canceling:");
        
                // Check if the reason is not empty
                if (reason != null && !reason.trim().isEmpty()) {
                    // Retrieve the values from the selected row using the correct column indexes
                    String name = (String) table.getValueAt(selectedRow, 0);  // Name column
                    String age = (String) table.getValueAt(selectedRow, 1);   // Age column
                    String contact = (String) table.getValueAt(selectedRow, 2);  // Contact column
                    String date = (String) table.getValueAt(selectedRow, 3);   // Date column
                    String time = (String) table.getValueAt(selectedRow, 4);   // Time column
                    String doctor = (String) table.getValueAt(selectedRow, 5); // Doctor column
        
                    // Create an array for the canceled appointment, including the user's reason
                    String[] canceledAppointment = {name, age, contact, date, time, doctor, reason};
        
                    // **Change this part**: Instead of calling the archived method, call the canceled appointment method
                    ArchivedAppointment ca = new ArchivedAppointment();
                    ca.archiveAppointment(canceledAppointment); // Make sure this is for cancelation, not archiving.
        
                    // Optionally, remove the row from the table after cancellation
                    DefaultTableModel model = (DefaultTableModel) table.getModel();  // Get the model
                    model.removeRow(selectedRow);  // Remove the selected row from the model
        
                    // Confirmation message after cancellation
                    JOptionPane.showMessageDialog(null, "Appointment has been canceled successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Reason cannot be empty.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an appointment to cancel.");
            }
        });
        
        // Enable the Archive button when a row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int[] selectedRows = table.getSelectedRows();
                cancelButton.setEnabled(selectedRows.length > 0);  // Enable button if at least one row is selected
            }
        });
    
        // Add the Archive button to the panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(cancelButton);
    
    
        // Place the button panel to the left of the table
        panel.add(buttonPanel, BorderLayout.WEST);
    
        // Exit Button (Red) at the bottom
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(255, 69, 58));  // Red background
        exitButton.setForeground(Color.WHITE);  // White text color
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));  // Smaller font size
        exitButton.setFocusPainted(false);  // Removes focus border
        exitButton.setPreferredSize(new Dimension(150, 30));  // Smaller button size
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appointmentFrame.dispose();  // Close the frame
              
    
                
            }
        });
    
        // Create a panel for the exit button at the bottom
        JPanel exitButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));  // Center the exit button
        exitButtonPanel.setOpaque(false);  // Transparent background
        exitButtonPanel.add(exitButton);  // Add the Exit button
    
        // Add the exit button panel to the bottom of the main panel
        panel.add(exitButtonPanel, BorderLayout.SOUTH);
    
        // Add the main panel to the frame
        appointmentFrame.add(panel);
        appointmentFrame.setVisible(true);
    }                     
} 