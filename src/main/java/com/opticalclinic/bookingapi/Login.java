package com.opticalclinic.bookingapi;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends OpticalClinicBookingSystem{
    private final UserManager userManager = new UserManager();
   
    public void showLoginPanel() {
        try {
            userManager.initializeAdmin();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error initializing admin: " + ex.getMessage());
            return;
        }

        JFrame frame = new JFrame("Login");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        ImageIcon image = new ImageIcon("C:\\IT2_APROJECT\\GUIProject\\src\\main\\java\\com\\opticalclinic\\bookingapi\\logo.png");
        frame.setIconImage(image.getImage());

        // Background panel
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

        // GridBag layout configuration
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(250, 30));
        panel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 30));
        panel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.white);
        loginButton.setFocusable(false);
       loginButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        try {
            // ✅ First: Check Admin
            Admin admin = userManager.authenticateAdmin(username, password);
            if (admin != null) {
                OpticalClinicBookingSystem.loggedInUsername = username;
                System.out.println("Admin logged in: " + admin.getAdminID());
                frame.dispose();
                ad.menu(); // or new AdminMenu(admin).menu();
                return;
            }

          // ✅ Second: Check Doctor
          Doctor doctor = userManager.authenticateDoctor(username, password);
          if (doctor != null) {
            OpticalClinicBookingSystem.loggedInUsername = username;
            System.out.println("Doctor logged in: " + doctor.getDoctorID());
            frame.dispose();
            //DoctorMenu doctorMenu = new DoctorMenu(doctor);  // Create the instance
            DoctorMenu dm = new DoctorMenu("not null");
            dm.menu();                   // Show the GUI through menu()
            return;
}


            // ✅ Third: Check Regular User
            User user = userManager.authenticateUser(username, password);
            if (user != null) {
                OpticalClinicBookingSystem.loggedInUsername = username;
                System.out.println("User logged in: " + user.getUsername());
                frame.dispose();
                us.menu(); // or new UserMenu(user).menu();
                return;
            }

            // ❌ No match
            JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
});

        
    // Cancel Button
    JButton cancelButton = new JButton("Cancel");
    cancelButton.setPreferredSize(new Dimension(120, 40));
    cancelButton.setBackground(Color.RED);
    cancelButton.setForeground(Color.white);
    cancelButton.setFocusable(false);
    cancelButton.addActionListener(e -> {
        frame.dispose();
        wp.showWelcomePanel(); // Go back to the welcome panel
    });

    // Button panel
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    buttonPanel.setOpaque(false);
    buttonPanel.add(loginButton);
    buttonPanel.add(cancelButton);

    // Add button panel to main panel
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    panel.add(buttonPanel, gbc);

    // Set up the frame and show the login panel
    frame.add(panel);
    frame.setVisible(true);
}
                   
 
   
}