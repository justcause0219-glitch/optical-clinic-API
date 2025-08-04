package com.opticalclinic.bookingapi;
/*import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
public class NotificationService {
private JLabel notificationLabel = new JLabel("Welcome!");
    public static void sendAppointmentConfirmation(String patientName) {
        String content = "Your appointment has been confirmed. Thank you!";
        Message message = new Message(patientName, content);
        message.send();
    }

    public static void sendAppointmentReminder(String patientName, String appointmentTime) {
        String content = "Reminder: You have an appointment at " + appointmentTime + ".";
        Message message = new Message(patientName, content);
        message.send();
    }

    public static void sendDoctorReschedule(String patientName, String newTime) {
        String content = "Your appointment has been rescheduled to " + newTime + ".";
        Message message = new Message(patientName, content);
        message.send();
    }

    public static void sendCustomMessage(String recipient, String customContent) {
        Message message = new Message(recipient, customContent);
        message.send();
    }

public void showNotification(String messageContent) {
    notificationLabel.setText("🔔 " + messageContent);
    JOptionPane.showMessageDialog(null, messageContent, "Notification", JOptionPane.INFORMATION_MESSAGE);
}
public void showNotificationHistory() {
        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        notificationPanel.setBackground(Color.WHITE);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
            String userQuery = "SELECT UserID FROM users WHERE Username = ?";
            int userId = -1;

            try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
                userStmt.setString(1, OpticalClinicBookingSystem.loggedInUsername);
                ResultSet rs = userStmt.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("UserID");
                }
            }

            if (userId != -1) {
                String query = "SELECT Message, Type, CreatedAt FROM notifications WHERE UserID = ? ORDER BY CreatedAt DESC";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        String type = rs.getString("Type");
                        String msg = rs.getString("Message");
                        String time = rs.getString("CreatedAt");

                        // Notification card
                        JPanel card = new JPanel(new BorderLayout());
                        card.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));
                        card.setBackground(new Color(245, 245, 245));
                        card.setMaximumSize(new Dimension(380, 100));

                        // Type badge
                        JLabel typeLabel = new JLabel(type.toUpperCase());
                        typeLabel.setOpaque(true);
                        typeLabel.setForeground(Color.WHITE);
                        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
                        typeLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

                        switch (type.toLowerCase()) {
                            case "info" -> typeLabel.setBackground(new Color(0, 123, 255));
                            case "warning" -> typeLabel.setBackground(new Color(255, 193, 7));
                            case "error" -> typeLabel.setBackground(new Color(220, 53, 69));
                            default -> typeLabel.setBackground(Color.GRAY);
                        }

                        // Message content
                        JLabel messageLabel = new JLabel("<html><body style='width:300px'>" + msg + "</body></html>");
                        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                        // Timestamp
                        JLabel timestampLabel = new JLabel(time);
                        timestampLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                        timestampLabel.setForeground(Color.DARK_GRAY);

                        // Layout assembly
                        JPanel top = new JPanel(new BorderLayout());
                        top.setOpaque(false);
                        top.add(typeLabel, BorderLayout.WEST);
                        top.add(timestampLabel, BorderLayout.EAST);

                        card.add(top, BorderLayout.NORTH);
                        card.add(messageLabel, BorderLayout.CENTER);

                        notificationPanel.add(card);
                        notificationPanel.add(Box.createVerticalStrut(10)); // spacing between cards
                    }
                }
            } else {
                notificationPanel.add(new JLabel("User not found or not logged in."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            notificationPanel.removeAll();
            notificationPanel.add(new JLabel("Error loading notifications."));
        }

        JScrollPane scrollPane = new JScrollPane(notificationPanel);
        scrollPane.setPreferredSize(new Dimension(420, 400));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        JOptionPane.showMessageDialog(null, scrollPane, "Notification History", JOptionPane.PLAIN_MESSAGE);
    }
}
