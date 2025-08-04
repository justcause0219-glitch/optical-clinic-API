package com.opticalclinic.bookingapi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager; 
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ManageAvailability {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Jessa@35";
    
protected void manageAvailability() {
    JFrame availabilityFrame = new JFrame("Manage Availability");
    availabilityFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    availabilityFrame.setVisible(true);

    availabilityFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    availabilityFrame.setLocationRelativeTo(null);

    // Outer panel for padding
    JPanel outerPanel = new JPanel(new BorderLayout());
    outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Inner panel for form layout
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridBagLayout());
    formPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Set Date Unavailable using combo boxes for year, month, and day
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.LINE_END;
    formPanel.add(new JLabel("Set Date Unavailable:"), gbc);

    JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

    // Date selectors
    JComboBox<String> yearComboBox = new JComboBox<>();
    for (int i = 2025; i <= 2050; i++) {
        yearComboBox.addItem(String.valueOf(i));
    }
    datePanel.add(yearComboBox);

    JComboBox<String> monthComboBox = new JComboBox<>();
    String[] months = {
        "January", "February", "March", "April", "May", "June", 
        "July", "August", "September", "October", "November", "December"
    };
    for (String month : months) {
        monthComboBox.addItem(month);
    }
    datePanel.add(monthComboBox);

    JComboBox<String> dayComboBox = new JComboBox<>();
    for (int i = 1; i <= 31; i++) {
        dayComboBox.addItem(String.format("%02d", i));
    }
    datePanel.add(dayComboBox);

    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.LINE_START;
    formPanel.add(datePanel, gbc);

    // Reason for Unavailability - placed below the date selection
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.LINE_END;
    formPanel.add(new JLabel("Reason for Unavailability:"), gbc);

    JComboBox<String> reasonComboBox = new JComboBox<>();
    reasonComboBox.addItem("Maintenance");
    reasonComboBox.addItem("Holiday");
    reasonComboBox.addItem("Staff Shortage");
    reasonComboBox.addItem("Other...");

    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.LINE_START;
    formPanel.add(reasonComboBox, gbc);

    // Buttons panel
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    buttonsPanel.setBackground(Color.WHITE);

    JButton saveButton = new JButton("Save Unavailability");
    saveButton.setBackground(new Color(0, 123, 255));
    saveButton.setForeground(Color.WHITE);
    saveButton.setFocusPainted(false);
    saveButton.setPreferredSize(new Dimension(150, 40));
    saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String unavailableYear = (String) yearComboBox.getSelectedItem();
            String unavailableMonth = String.format("%02d", monthComboBox.getSelectedIndex() + 1); // Convert month to 2-digit format
            String unavailableDay = (String) dayComboBox.getSelectedItem();
            String unavailableDate = unavailableYear + "-" + unavailableMonth + "-" + unavailableDay; // Format as YYYY-MM-DD
            String reason = (String) reasonComboBox.getSelectedItem();
    
            // Save the date and reason to the database
            try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String query = "INSERT INTO unavailable_dates (date, reason) VALUES (?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, unavailableDate); // Set the date
                    stmt.setString(2, reason);         // Set the reason
                    stmt.executeUpdate();              // Execute the query
    
                    // Display confirmation
                    JOptionPane.showMessageDialog(
                        availabilityFrame,
                        "Unavailability saved:\nDate: " + unavailableDate + "\nReason: " + reason,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    availabilityFrame,
                    "Failed to save unavailability:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
    
            availabilityFrame.dispose(); // Close the frame after saving
        }
    });
    
    buttonsPanel.add(saveButton);

    JButton exitButton = new JButton("Exit");
    exitButton.setBackground(new Color(220, 53, 69));
    exitButton.setForeground(Color.WHITE);
    exitButton.setFocusPainted(false);
    exitButton.setPreferredSize(new Dimension(150, 40));
    exitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            availabilityFrame.dispose();
        }
    });
    buttonsPanel.add(exitButton);

    // Add components to the outer panel
    outerPanel.add(formPanel, BorderLayout.CENTER);
    outerPanel.add(buttonsPanel, BorderLayout.SOUTH);

    availabilityFrame.add(outerPanel);
    availabilityFrame.setVisible(true);
}
}