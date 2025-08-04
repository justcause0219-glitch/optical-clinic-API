package com.opticalclinic.bookingapi;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePanelGui extends OpticalClinicBookingSystem {
    // Method to display the welcome panel
    Login log = new Login();
    
    public void showWelcomePanel() {
        JFrame frame = new JFrame("OCBS");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        // Set logo and background
        ImageIcon image = new ImageIcon("C:\\IT2_APROJECT\\GUIProject\\src\\main\\java\\com\\opticalclinic\\bookingapi\\logo.png");
        frame.setIconImage(image.getImage());

        // Create panel and layout
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("C:\\IT2_APROJECT\\GUIProject\\src\\main\\java\\com\\opticalclinic\\bookingapi\\bgpho.jpg");
                Image img = backgroundImage.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setOpaque(false);

        // Add label and buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel welcomeLabel = new JLabel("Welcome to Optical Clinic \nBooking System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 35));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(new Color(0, 0, 0, 150));
        welcomeLabel.setPreferredSize(new Dimension(800, 150));
        panel.add(welcomeLabel, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setOpaque(false);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(150, 50));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 20));
        loginButton.setFocusable(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Dispose of the welcome frame
               
                log.showLoginPanel();
            }
        });

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 128, 0));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(150, 50));
        registerButton.setFont(new Font("Arial", Font.PLAIN, 20));
        registerButton.setFocusable(false);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Dispose of the welcome frame
                ur.showRegisterPanel(null);
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }
}