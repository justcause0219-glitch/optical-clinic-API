package com.opticalclinic.bookingapi;
import javax.swing.SwingUtilities;

public class OpticalClinicBookingSystem {
    static AdminMenu ad = new AdminMenu();
    static UserMenu us = new UserMenu();
    static UserManager um = new UserManager();
   // User user = new User(null, null, null, null, null);
    static String time = new String();
    static UserRegistration ur = new UserRegistration();
    static WelcomePanelGui wp = new WelcomePanelGui();
    static Login log = new Login();
    UserViewAppointment uva = new UserViewAppointment();
    
ManageAvailability ma = new ManageAvailability();
AdminViewAppointment ava = new AdminViewAppointment();
static ArchivedAppointment aa = new ArchivedAppointment();
public static String loggedInUsername = "";  // Set this on login


static final String JDBC_URL = "jdbc:mysql://localhost:3306/project";
static final String DB_USER = "root";
 static final String DB_PASSWORD = "Jessa@35";
    // Main method to launch the application
   public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WelcomePanelGui welcomePanel = new WelcomePanelGui(); // Create object representation
                
                welcomePanel.showWelcomePanel(); // Show the GUI
            }
        });
    }
}
