package com.opticalclinic.bookingapi;


import javax.swing.*;

import java.awt.*;


public class UserRegistration extends OpticalClinicBookingSystem {

    private WelcomePanelGui welcomePanel;
    
    public UserRegistration() {

        this.welcomePanel = new WelcomePanelGui(); // Provide a default value
    }
 
    public void showRegisterPanel(Client client) {
            
            JFrame frame = new JFrame("Register");
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
    
            ImageIcon image = new ImageIcon("C:\\IT2_APROJECT\\GUIProject\\src\\main\\java\\com\\opticalclinic\\bookingapi\\logo.png");
            frame.setIconImage(image.getImage());
    
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
            panel.setBackground(new Color(0, 0, 0, 150));
    
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
    
            // Username
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Username:"), gbc);
    
            gbc.gridx = 1;
            JTextField usernameField = new JTextField();
            usernameField.setPreferredSize(new Dimension(250, 30));
            panel.add(usernameField, gbc);
    
            // Password
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Password:"), gbc);
    
            gbc.gridx = 1;
            JPasswordField passwordField = new JPasswordField();
            passwordField.setPreferredSize(new Dimension(250, 30));
            panel.add(passwordField, gbc);
    
            // Confirm Password
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Confirm Password:"), gbc);
    
            gbc.gridx = 1;
            JPasswordField confirmPasswordField = new JPasswordField();
            confirmPasswordField.setPreferredSize(new Dimension(250, 30));
            panel.add(confirmPasswordField, gbc);
    
            // Full Name
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Full Name:"), gbc);
    
            gbc.gridx = 1;
            JTextField nameField = new JTextField();
            nameField.setPreferredSize(new Dimension(250, 30));
            panel.add(nameField, gbc);
    
            // Phone Number
            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Phone Number:"), gbc);
    
            gbc.gridx = 1;
            JTextField phoneField = new JTextField();
            phoneField.setPreferredSize(new Dimension(250, 30));
            panel.add(phoneField, gbc);
    
            // Birthdate
            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(new JLabel("Birthdate:"), gbc);
    
            JPanel birthdatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JComboBox<String> yearComboBox = new JComboBox<>();
            for (int i = 1950; i <= 2024; i++) {
                yearComboBox.addItem(String.valueOf(i));
            }
            birthdatePanel.add(yearComboBox);
    
            JComboBox<String> monthComboBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            });
            birthdatePanel.add(monthComboBox);
    
            JComboBox<String> dayComboBox = new JComboBox<>();
            for (int i = 1; i <= 31; i++) {
                dayComboBox.addItem(String.format("%02d", i));
            }
            birthdatePanel.add(dayComboBox);
    
            gbc.gridx = 1;
            panel.add(birthdatePanel, gbc);
    
            // Buttons
            JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            buttonPanel.setOpaque(false);
    
            JButton registerButton = new JButton("Register");
            registerButton.setPreferredSize(new Dimension(120, 40));
            registerButton.setBackground(new Color(0, 123, 255));
            registerButton.setForeground(Color.white);
            registerButton.setFocusable(false);
           registerButton.addActionListener(e -> {
    um.handleRegistrationUser(frame, usernameField, passwordField, confirmPasswordField,
                              nameField, phoneField, yearComboBox, monthComboBox,
                              dayComboBox, client);
});
                
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.white);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(e -> {
            frame.dispose();
            welcomePanel.showWelcomePanel();
        });


        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }
}
