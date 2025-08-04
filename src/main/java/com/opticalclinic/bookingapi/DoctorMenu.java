package com.opticalclinic.bookingapi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DoctorMenu extends OpticalClinicBookingSystem implements MenuBase {
    //private JTable table;
    private DefaultTableModel model;
    private String doctorName;

    public DoctorMenu(String doctorName) {
        this.doctorName = doctorName;
        menu();
    }

    public void menu() {
    JFrame frame = new JFrame("Doctor Menu - Optical Clinic Booking System");
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

    ImageIcon image = new ImageIcon("C:\\IT2_APROJECT\\GUIProject\\src\\main\\java\\com\\opticalclinic\\bookingapi\\logo.png");
    frame.setIconImage(image.getImage());

    JPanel panel = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon backgroundImage = new ImageIcon("C:\\IT2_APROJECT\\GUIProject\\src\\main\\java\\com\\opticalclinic\\bookingapi\\bgpho.jpg");
            Image img = backgroundImage.getImage();
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    };

    // ========== SIDEBAR ==========
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new GridBagLayout());
    leftPanel.setPreferredSize(new Dimension(200, 800));
    leftPanel.setBackground(new Color(211, 211, 211));
    leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.insets = new Insets(10, 0, 10, 0);

    // View Appointments
    JButton viewAppointmentsBtn = new JButton("View Appointments");
    styleSidebarButton(viewAppointmentsBtn);
    viewAppointmentsBtn.addActionListener(e -> loadAppointments());
    gbc.gridy = 0;
    leftPanel.add(viewAppointmentsBtn, gbc);

    // Refresh (reload today's summary)
    JButton refreshBtn = new JButton("Refresh");
    styleSidebarButton(refreshBtn);
    String doctorName = "not null";
    refreshBtn.addActionListener(e -> {
        panel.remove(1);  // Remove center panel
        JPanel refreshed = getTodaysAppointmentsPanel(doctorName);
        panel.add(refreshed, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    });
    gbc.gridy = 1;
    leftPanel.add(refreshBtn, gbc);

    // Logout
    JButton logoutBtn = new JButton("Logout");
    styleSidebarButton(logoutBtn);
    logoutBtn.addActionListener(e -> {
        frame.dispose();
        log.showLoginPanel();
    });
    gbc.gridy = 2;
    leftPanel.add(logoutBtn, gbc);

    panel.add(leftPanel, BorderLayout.WEST);

    // ========== CENTER: Today's Appointments or Message ==========
    if (hasAppointmentsToday(doctorName)) {
        JPanel centerPanel = getTodaysAppointmentsPanel(doctorName);
        panel.add(centerPanel, BorderLayout.CENTER);
    } else {
        JLabel noAppointments = new JLabel("No appointments for today.", SwingConstants.CENTER);
        noAppointments.setFont(new Font("Arial", Font.ITALIC, 16));
        panel.add(noAppointments, BorderLayout.CENTER);
    }

    frame.add(panel);
    frame.setVisible(true);
}

    private void loadAppointments() {
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

    // Archive Button (Green)
    JButton archiveButton = new JButton("Archive");
    archiveButton.setBackground(new Color(34, 139, 34));  // Green background
    archiveButton.setForeground(Color.WHITE);  // White text color
    archiveButton.setFont(new Font("Arial", Font.BOLD, 12));  // Smaller font size
    archiveButton.setFocusPainted(false);  // Removes focus border
    archiveButton.setPreferredSize(new Dimension(150, 30));  // Smaller button size
    archiveButton.setEnabled(false);  // Initially disabled
    archiveButton.setFocusable(false);
archiveButton.addActionListener(e -> {
    int selectedRow = table.getSelectedRow();  // Get the selected row index

    // Check if a row is selected
    if (selectedRow != -1) {
        // Retrieve the values from the selected row using the correct column indexes
        String name = (String) table.getValueAt(selectedRow, 0);  // Name column
        String age = (String) table.getValueAt(selectedRow, 1);   // Age column
        String contact = (String) table.getValueAt(selectedRow, 2);  // Contact column
        String date = (String) table.getValueAt(selectedRow, 3);   // Date column
        String time = (String) table.getValueAt(selectedRow, 4);   // Time column
        String doctor = (String) table.getValueAt(selectedRow, 5); // Doctor column
        String reason = "Archived by Admin";

        // Create an array for the appointment
        String[] appointment = {name, age, contact, date, time, doctor, reason};

        // Create an instance of ArchivedAppointment and call the method
        ArchivedAppointment aa = new ArchivedAppointment();
        aa.archiveAppointment(appointment);

        // Optionally, remove the row from the table after archiving
        DefaultTableModel model = (DefaultTableModel) table.getModel();  // Get the model
        model.removeRow(selectedRow);  // Remove the selected row from the model
    } else {
        JOptionPane.showMessageDialog(null, "Please select an appointment to archive.");
    }
});

    // Enable the Archive button when a row is selected
    table.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int[] selectedRows = table.getSelectedRows();
            archiveButton.setEnabled(selectedRows.length > 0);  // Enable button if at least one row is selected
        }
    });

    // Add the Archive button to the panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    buttonPanel.add(archiveButton);

    // Place the button panel to the left of the table
    panel.add(buttonPanel, BorderLayout.WEST);


    // Define options for the doctor/admin
String[] actions = {"Select Action", "Approve","Declined" ,"Request Reschedule", "Send Message"};

// Create a JComboBox for choices
JComboBox<String> actionDropdown = new JComboBox<>(actions);
actionDropdown.setPreferredSize(new Dimension(160, 30));

// Action button
JButton performActionButton = new JButton("Submit");
performActionButton.setPreferredSize(new Dimension(100, 30));
performActionButton.setBackground(new Color(34, 139, 34)); // Green
performActionButton.setForeground(Color.WHITE);
performActionButton.setFocusable(false);

// Action handling
performActionButton.addActionListener(e -> {
    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Please select an appointment first.");
        return;
    }

    String selectedAction = (String) actionDropdown.getSelectedItem();

    if (selectedAction.equals("Select Action")) {
        JOptionPane.showMessageDialog(null, "Please choose an action.");
        return;
    }

    // Extract data
    String patientName = (String) table.getValueAt(selectedRow, 0);
    String date = (String) table.getValueAt(selectedRow, 3);
    String time = (String) table.getValueAt(selectedRow, 4);

    // Based on selected action
    switch (selectedAction) {
        case "Approve":
            sendNotificationToUser(patientName, "Your appointment on " + date + " at " + time + " has been approved.", "approval");
            JOptionPane.showMessageDialog(null, "Appointment approved and user notified.");
            break;
        case "Declined":
            sendNotificationToUser(patientName, "Your appointment on " + date + " at " + time + " has been declined. Visit clinic for more info about the denial", "approval");
            JOptionPane.showMessageDialog(null, "Appointment declined and user notified.");
            break;
        case "Request Reschedule":
            String newDate = JOptionPane.showInputDialog(null, "Enter new date for reschedule:");
            if (newDate != null && !newDate.trim().isEmpty()) {
                sendNotificationToUser(patientName, "Doctor requested to reschedule your appointment to: " + newDate, "reschedule");
                JOptionPane.showMessageDialog(null, "Reschedule request sent.");
            }
            break;

        case "Send Message":
            String message = JOptionPane.showInputDialog(null, "Enter message to send to patient:");
            if (message != null && !message.trim().isEmpty()) {
                sendNotificationToUser(patientName, message, "message");
                JOptionPane.showMessageDialog(null, "Message sent to patient.");
            }
            break;
    }
});

    buttonPanel.add(actionDropdown);
    buttonPanel.add(performActionButton);


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
    private void styleSidebarButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    private void sendNotificationToUser(String userName, String message, String type) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
        String userQuery = "SELECT UserID FROM users WHERE FullName = ?";
        int userId = -1;

        try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
            userStmt.setString(1, userName);
            ResultSet rs = userStmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("UserID");
            }
        }

        if (userId != -1) {
            String insertQuery = "INSERT INTO notifications (UserID, Message, Type) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setInt(1, userId);
                stmt.setString(2, message);
                stmt.setString(3, type);
                stmt.executeUpdate();
            }
        } else {
            System.out.println("User not found for: " + userName);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
private JPanel getTodaysAppointmentsPanel(String doctorName) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);

    JLabel title = new JLabel("Today's Appointments", SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 18));
    panel.add(title, BorderLayout.NORTH);

    DefaultTableModel todayModel = new DefaultTableModel(new String[]{"Time", "Patient", "Contact", "Status"}, 0);
    JTable todayTable = new JTable(todayModel);

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
        String sql = "SELECT ts.Time, u.FullName, u.PhoneNumber, a.Status " +
                     "FROM appointments a " +
                     "JOIN users u ON a.UserID = u.UserID " +
                     "JOIN timeslots ts ON a.TimeSlotID = ts.TimeSlotID " +
                     "JOIN doctors d ON a.DoctorID = d.DoctorID " +
                     "WHERE a.AppointmentDate = CURDATE() AND d.Name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                todayModel.addRow(new Object[]{
                    rs.getString("Time"),
                    rs.getString("FullName"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Status")
                });
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    JScrollPane scrollPane = new JScrollPane(todayTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
}
private boolean hasAppointmentsToday(String doctorName) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
        String sql = "SELECT COUNT(*) FROM appointments a " +
                     "JOIN doctors d ON a.DoctorID = d.DoctorID " +
                     "WHERE a.AppointmentDate = CURDATE() AND d.Name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false;
}

}
