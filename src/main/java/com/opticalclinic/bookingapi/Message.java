package com.opticalclinic.bookingapi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JOptionPane;

public class Message {
    private String recipient;      // e.g., Patient or Doctor name
    private String content;        // The message content
    private String timestamp;      // Time message was created

    public Message(String recipient, String content) {
        this.recipient = recipient;
        this.content = content;
        this.timestamp = generateTimestamp();
    }

    private String generateTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void send() {
        System.out.println("🔔 Notification to: " + recipient);
        System.out.println("📨 " + content);
        System.out.println("🕒 Sent at: " + timestamp);
        System.out.println("-----------------------------------");
    }

public static List<String> getSentMessages(String role) {
    List<String> messages = new ArrayList<>();

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {

        String query = "SELECT u.FullName, n.Message, n.Type, n.CreatedAt " +
                       "FROM notifications n " +
                       "JOIN users u ON n.UserID = u.UserID " +
                       "WHERE n.Type = 'Cancellation' " +  // You can adjust the filter if needed
                       "ORDER BY n.CreatedAt DESC";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String user = rs.getString("FullName");
                String message = rs.getString("Message");
                String type = rs.getString("Type");
                String time = rs.getString("CreatedAt");

                messages.add("[" + time + "] " + user + " (" + type + "): " + message);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        messages.add("Error loading messages from users.");
    }

    return messages;
}
public static void logCancellation(int userId, int appointmentId, String reason) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Jessa@35")) {
        String sql = "INSERT INTO cancellations (UserID, AppointmentID, Reason) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, appointmentId);
            stmt.setString(3, reason);
            stmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error saving cancellation to database.");
    }
}


    // You can add getters/setters later if needed
}
