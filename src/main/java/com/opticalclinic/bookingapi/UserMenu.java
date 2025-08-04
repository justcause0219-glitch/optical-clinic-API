package com.opticalclinic.bookingapi;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class UserMenu extends OpticalClinicBookingSystem implements MenuBase {

    BookingForm bf = new BookingForm();
    NotificationService nf = new NotificationService();
    static UserViewAppointment uva = new UserViewAppointment();

    // Store messages
    private List<String> notificationHistory = new ArrayList<>();

    // Optional: for displaying latest message in the main UI
    private JLabel notificationLabel = new JLabel("Welcome!");

    public void menu() {
        JFrame frame = new JFrame("User Menu - Optical Clinic Booking System");
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
        panel.setOpaque(false);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(200, frame.getHeight()));
        leftPanel.setBackground(new Color(211, 211, 211));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton bookAppointmentButton = new JButton("Book Appointment");
        bookAppointmentButton.setPreferredSize(new Dimension(150, 40));
        bookAppointmentButton.setBackground(new Color(0, 123, 255));
        bookAppointmentButton.setForeground(Color.WHITE);
        bookAppointmentButton.setFocusable(false);
        bookAppointmentButton.addActionListener(new ActionListener() {
            private Client client;

            @Override
            public void actionPerformed(ActionEvent e) {
                bf.openBookingForm(client);
                showNotification("Appointment booking form opened.");
            }
        });
        gbc.gridy = 1;
        leftPanel.add(bookAppointmentButton, gbc);

        JButton viewAppointmentsButton = new JButton("View Appointments");
        viewAppointmentsButton.setPreferredSize(new Dimension(150, 40));
        viewAppointmentsButton.setBackground(new Color(0, 123, 255));
        viewAppointmentsButton.setForeground(Color.WHITE);
        viewAppointmentsButton.setFocusable(false);
        viewAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uva.viewAppointmentsUser();
                showNotification("Viewing your appointments.");
            }
        });
        gbc.gridy = 2;
        leftPanel.add(viewAppointmentsButton, gbc);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.setBackground(new Color(0, 123, 255));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                log.showLoginPanel();
            }
        });
        gbc.gridy = 3;
        leftPanel.add(logoutButton, gbc);

        // ✅ Add Notifications button
        JButton notificationsButton = new JButton("Notifications");
        notificationsButton.setPreferredSize(new Dimension(150, 40));
        notificationsButton.setBackground(new Color(0, 123, 255));
        notificationsButton.setForeground(Color.WHITE);
        notificationsButton.setFocusable(false);
        notificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nf.showNotificationHistory();
            }
        });
        gbc.gridy = 4;
        leftPanel.add(notificationsButton, gbc);

        // ✅ Add notification label at the bottom
        notificationLabel.setForeground(Color.BLUE);
        panel.add(notificationLabel, BorderLayout.SOUTH);

        panel.add(leftPanel, BorderLayout.WEST);
        frame.add(panel);
        frame.setVisible(true);
    }

    // ✅ Append notification and update UI label
    public void showNotification(String messageContent) {
        notificationHistory.add(messageContent);
        notificationLabel.setText("🔔 " + messageContent);
    }

   
}
