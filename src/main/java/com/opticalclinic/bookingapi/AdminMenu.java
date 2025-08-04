package com.opticalclinic.bookingapi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminMenu extends OpticalClinicBookingSystem implements MenuBase {
    AddEmployee addEmployee = new AddEmployee();
    private Admin admin;
    JTable table;
    DefaultTableModel model;

    public void menu() {
        JFrame frame = new JFrame("Admin Menu - Optical Clinic Booking System");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

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

        // ===================== SIDEBAR LEFT PANEL =====================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(200, 800));
        leftPanel.setBackground(new Color(211, 211, 211));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // View Appointments
        JButton viewAppointmentsButton = new JButton("View Appointments");
        viewAppointmentsButton.setPreferredSize(new Dimension(150, 40));
        viewAppointmentsButton.setBackground(new Color(0, 123, 255));
        viewAppointmentsButton.setForeground(Color.WHITE);
        viewAppointmentsButton.setFocusable(false);
        viewAppointmentsButton.addActionListener(e -> ava.viewAppointmentsAdmin(admin));
        gbc.gridy = 0;
        leftPanel.add(viewAppointmentsButton, gbc);

        // Manage Availability
        JButton manageAvailabilityButton = new JButton("Manage Availability");
        manageAvailabilityButton.setPreferredSize(new Dimension(150, 40));
        manageAvailabilityButton.setBackground(new Color(0, 123, 255));
        manageAvailabilityButton.setForeground(Color.WHITE);
        manageAvailabilityButton.setFocusable(false);
        manageAvailabilityButton.addActionListener(e -> ma.manageAvailability());
        gbc.gridy = 1;
        leftPanel.add(manageAvailabilityButton, gbc);

        // View Archive
        JButton viewArchiveButton = new JButton("View Archive");
        viewArchiveButton.setPreferredSize(new Dimension(150, 40));
        viewArchiveButton.setBackground(new Color(0, 123, 255));
        viewArchiveButton.setForeground(Color.WHITE);
        viewArchiveButton.setFocusable(false);
        viewArchiveButton.addActionListener(e -> {
            frame.dispose();
            aa.viewArchivedAppointments();
        });
        gbc.gridy = 2;
        leftPanel.add(viewArchiveButton, gbc);

        // Register Employee
        JButton registerEmployeeButton = new JButton("Register Employee");
        registerEmployeeButton.setPreferredSize(new Dimension(150, 40));
        registerEmployeeButton.setBackground(new Color(0, 123, 255));
        registerEmployeeButton.setForeground(Color.WHITE);
        registerEmployeeButton.setFocusable(false);
        registerEmployeeButton.addActionListener(new ActionListener() {
            private Employee employee;
            
          @Override
            public void actionPerformed(ActionEvent e) {
                 frame.dispose();
                addEmployee.showRegisterPanel(employee);
            }
        });
        gbc.gridy = 3;
        leftPanel.add(registerEmployeeButton, gbc);

        // Logout
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.setBackground(new Color(0, 123, 255));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(e -> {
            frame.dispose();
            log.showLoginPanel();
        });
        gbc.gridy = 4;
        leftPanel.add(logoutButton, gbc);

        panel.add(leftPanel, BorderLayout.WEST);
        frame.add(panel);
        frame.setVisible(true);
    }

    public void showNotificationHistory() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {

            String query = """
                SELECT u.FullName, a.Date, a.Time, a.Doctor, c.Reason, c.CancelledAt
                FROM cancellations c
                JOIN users u ON c.UserID = u.UserID
                JOIN appointments a ON c.AppointmentID = a.AppointmentID
                ORDER BY c.CancelledAt DESC
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String name = rs.getString("FullName");
                    String date = rs.getString("Date");
                    String time = rs.getString("Time");
                    String doctor = rs.getString("Doctor");
                    String reason = rs.getString("Reason");
                    String cancelledAt = rs.getString("CancelledAt");

                    textArea.append("[" + cancelledAt + "] "
                            + name + " canceled an appointment with Dr. " + doctor
                            + " on " + date + " at " + time + ".\nReason: " + reason + "\n\n");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            textArea.setText("Error retrieving cancellation notifications.");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scrollPane, "Cancellation Notifications", JOptionPane.INFORMATION_MESSAGE);
    }
}
