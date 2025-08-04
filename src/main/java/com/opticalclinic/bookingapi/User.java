package com.opticalclinic.bookingapi;

import java.util.ArrayList;
import java.util.List;

public class User extends Person {
    private String username;
    private String password;
    private String birthdate;

    public User(String fullName, String phoneNumber, String username, String password, String birthdate) {
        super(fullName, phoneNumber);
        this.username = username;
        this.password = password;
        this.birthdate = birthdate;
    }

    


    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
  
    public String getPassword() {
        return password;
    }
    

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String getRole() {
        return "User";
    }

    // Validation methods
    public boolean isValidPhilippineNumber() {
        return getPhoneNumber().matches("^09[0-9]{9}$");
    }

    public boolean isValidPassword() {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$");
    }

    public int getUserID() {
            return getUserID();
       
       
    }


    public List<String> toCsvRow() {
    List<String> row = new ArrayList<>();
    row.add(getFullName());
    row.add(getPhoneNumber());
    row.add(getUsername());
    row.add(getPassword());
    row.add(getBirthdate());
    return row;
}

}
