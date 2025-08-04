package com.opticalclinic.bookingapi;
import javax.swing.*;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingForm extends JFrame implements LoginPanel {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Jessa@35";


    private static JTextField nameField;
    private static JTextField ageField;
    private static JTextField contactField;
    private static JComboBox<String> doctorComboBox;
    private static JComboBox<String> timeComboBox;
    
  
    //naa ni syay parameter na Client client
    protected String openBookingForm(Client client) {
        JFrame bookingFrame = new JFrame("Booking Appointment");
        bookingFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        bookingFrame.setVisible(true);

        bookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bookingFrame.setLocationRelativeTo(null); // Center the frame on the screen
    
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
    
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Input fields for patient details
        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);
    
        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);
    
        panel.add(new JLabel("Contact Number:"));
        contactField = new JTextField();
        panel.add(contactField);
    
        // Drop-down list for doctor selection
        panel.add(new JLabel("Choose Doctor:"));
        String[] doctors = {"Dr. John Doe", "Dr. Jane Smith"};
        doctorComboBox = new JComboBox<>(doctors);
        panel.add(doctorComboBox);
    
        // Drop-down list for time slots
        panel.add(new JLabel("Choose Time:"));
        String[] timeSlots = {"9:00 AM", "11:00 AM", "2:00 PM", "4:00 PM"};
        timeComboBox = new JComboBox<>(timeSlots);
        panel.add(timeComboBox);
    
        // Panel for selecting date (Year, Month, Day)
        panel.add(new JLabel("Choose Date (yyyy-mm-dd):"));
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    
        Integer[] years = new Integer[101];
        for (int i = 0; i < years.length; i++) {
            years[i] = 2025 + i;
        }
        JComboBox<Integer> yearComboBox = new JComboBox<>(years);
        datePanel.add(yearComboBox);
    
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        datePanel.add(monthComboBox);
    
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        datePanel.add(dayComboBox);
        panel.add(datePanel);
    
        JTextField dateField = new JTextField();
        dateField.setEditable(false);
        panel.add(dateField);
    
        yearComboBox.addActionListener(e -> updateDateField(yearComboBox, monthComboBox, dayComboBox, dateField));
        monthComboBox.addActionListener(e -> updateDateField(yearComboBox, monthComboBox, dayComboBox, dateField));
        dayComboBox.addActionListener(e -> {
            int year = (int) yearComboBox.getSelectedItem();
            int day = (int) dayComboBox.getSelectedItem();
            String formattedDate = String.format("%04d-%02d-%02d", year, monthComboBox.getSelectedIndex() + 1, day);
            dateField.setText(formattedDate);
        });
    
        contentPanel.add(panel, BorderLayout.CENTER);
    
        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
    
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0, 123, 255));
        submitButton.setForeground(Color.white);
        submitButton.setFocusable(false);
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String age = ageField.getText();
            String contact = contactField.getText();
            String doctor = (String) doctorComboBox.getSelectedItem();
            String time = (String) timeComboBox.getSelectedItem();
            String date = dateField.getText();
            
    
            // Validate form fields
            if (name.isEmpty() || age.isEmpty() || contact.isEmpty() || doctor == null || time == null || date.isEmpty()) {
                JOptionPane.showMessageDialog(bookingFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            try {
                Integer.parseInt(age); // Validate age as integer
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(bookingFrame, "Please enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (!contact.matches("^09[0-9]{9}$")) {
                JOptionPane.showMessageDialog(bookingFrame, "Please enter a valid contact number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (date.equals("Invalid Date")) {
                JOptionPane.showMessageDialog(bookingFrame, "Please select a valid date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Database connection and query execution
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                // Check for unavailable date
                String unavailableQuery = "SELECT reason FROM unavailable_dates WHERE date = ?";
                try (PreparedStatement unavailableStmt = conn.prepareStatement(unavailableQuery)) {
                    unavailableStmt.setString(1, date);
                    ResultSet unavailableResult = unavailableStmt.executeQuery();
                    if (unavailableResult.next()) {
                        String reason = unavailableResult.getString("reason");
                        JOptionPane.showMessageDialog(bookingFrame,
                                "The selected date (" + date + ") is unavailable.\nReason: " + reason,
                                "Unavailable Date",
                                JOptionPane.ERROR_MESSAGE);
                        return; // Exit the action listener
                    }
                }
    
                int doctorID = -1;
                String doctorQuery = "SELECT DoctorID FROM doctors WHERE Name = ?";
                try (PreparedStatement doctorStmt = conn.prepareStatement(doctorQuery)) {
                    doctorStmt.setString(1, doctor);
                    ResultSet doctorResult = doctorStmt.executeQuery();
                    if (doctorResult.next()) {
                        doctorID = doctorResult.getInt("DoctorID");
                    } else {
                        JOptionPane.showMessageDialog(bookingFrame, "Selected doctor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
    
                int timeSlotID = -1;
                String timeQuery = "SELECT TimeSlotID FROM timeslots WHERE Time = ?";
                try (PreparedStatement timeStmt = conn.prepareStatement(timeQuery)) {
                    timeStmt.setString(1, time);
                    ResultSet timeResult = timeStmt.executeQuery();
                    if (timeResult.next()) {
                        timeSlotID = timeResult.getInt("TimeSlotID");
                    }
                }
    
                String checkBookingQuery = "SELECT COUNT(*) FROM appointments WHERE DoctorID = ? AND AppointmentDate = ? AND TimeSlotID = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkBookingQuery)) {
                    checkStmt.setInt(1, doctorID);
                    checkStmt.setString(2, date);
                    checkStmt.setInt(3, timeSlotID);
                    ResultSet checkResult = checkStmt.executeQuery();
                    if (checkResult.next() && checkResult.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(bookingFrame, "This time slot is already booked for the selected doctor.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
    
                // Retrieve user ID
                String userQuery = "SELECT UserID, FullName FROM users WHERE FullName = ?";
                try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
                    userStmt.setString(1, name.trim()); // Trim input
                    ResultSet resultSet = userStmt.executeQuery();
    
                    int userID = -1;
                    String fullName = "";
                    if (resultSet.next()) {
                        userID = resultSet.getInt("UserID"); // Retrieve existing userID from the database
                        fullName = resultSet.getString("FullName"); // Get FullName
                    }
    
                    // If user doesn't exist, show error
                    if (userID == -1) {
                        JOptionPane.showMessageDialog(bookingFrame, "User not found. Please sign up first.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
    
                    // Insert the appointment, including FullName
                    String appointmentQuery = "INSERT INTO appointments (UserID, DoctorID, TimeSlotID, AppointmentDate, FullName) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement appointmentStmt = conn.prepareStatement(appointmentQuery)) {
                        appointmentStmt.setInt(1, userID);
                        appointmentStmt.setInt(2, doctorID);
                        appointmentStmt.setInt(3, timeSlotID);
                        appointmentStmt.setString(4, date);
                        appointmentStmt.setString(5, fullName);  // Store FullName
                        appointmentStmt.executeUpdate();

                        Booking booking = new Booking(fullName, age, contact, doctor, time, date);
                       List<Booking> bookings = new ArrayList<>();
                       bookings.add(booking);
                       CsvBookingWriter.writeBookingsToCsv(bookings, "appointments.csv");
                       XmlBookingWriter.writeBookingsToXml(bookings, "appointments.xml");
                    }
     
                    JOptionPane.showMessageDialog(bookingFrame,
                            "Booking Application Submitted. Waiting for Confirmation!\n" +
                            "Name: " + fullName + "\n" +
                            "Age: " + age + "\n" +
                            "Contact: " + contact + "\n" +
                            "Doctor: " + doctor + "\n" +
                            "Time: " + time + "\n" +
                            "Date: " + date,
                            "Booking Confirmation",
                            JOptionPane.INFORMATION_MESSAGE);
    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(bookingFrame, "Error connecting to the database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(bookingFrame, "Error connecting to the database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    
        buttonPanel.add(submitButton);
    
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.white);
        exitButton.setFocusable(false);
        exitButton.addActionListener(e -> bookingFrame.dispose());
        buttonPanel.add(exitButton);
    
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        bookingFrame.add(contentPanel);
        bookingFrame.setVisible(true);
        return null;
    }
    
    private static void updateDateField(JComboBox<Integer> yearComboBox, JComboBox<String> monthComboBox, JComboBox<Integer> dayComboBox, JTextField dateField) {
        int year = (int) yearComboBox.getSelectedItem();
        String month = (String) monthComboBox.getSelectedItem();
        int daysInMonth = getDaysInMonth(month, year);
    
        // Populate the dayComboBox with the correct number of days
        dayComboBox.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
            dayComboBox.addItem(i);
        }
    
        // Select the first day by default if no day is currently selected
        if (dayComboBox.getItemCount() > 0) {
            dayComboBox.setSelectedIndex(0); // Select the first day (index 0)
        }
    
        int day = (int) dayComboBox.getSelectedItem();
        
        // Format date as yyyy-MM-dd
        String formattedDate = String.format("%04d-%02d-%02d", year, monthComboBox.getSelectedIndex() + 1, day);
        dateField.setText(formattedDate);
    }
    private static int getDaysInMonth(String month, int year) {
        switch (month) {
            case "January": case "March": case "May": case "July": case "August": case "October": case "December":
                return 31;
            case "April": case "June": case "September": case "November":
                return 30;
            case "February":
                // Check for leap year
                return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }
    
            
            }