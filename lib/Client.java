package lib;

import com.opticalclinic.bookingapi.model.Entity;
import com.opticalclinic.bookingapi.model.Person;

//import jakarta.persistence.Entity;

@Entity
public class Client extends Person {

    private String username;
    private String password;
    private String confirmPassword; // Optional: can be removed or used for validation
    private String birthdate;

    //public Client() {}

    public Client(String fullName, String phoneNumber, String username, String password, String birthdate) {
        super(fullName, phoneNumber);
        this.username = username;
        this.password = password;
        this.birthdate = birthdate;
    }
/* 
    @Override
    public String getRole() {
        return "Client";
    }
*/
    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}


/* package com.opticalclinic.bookingapi.model;

@Entity
public class Client extends Person {

    private String username;
    private String password;
    private String confirmPassword; // Optional: can be removed or used for validation
    private String birthdate;

    //public Client() {}

    public Client(String fullName, String phoneNumber, String username, String password, String birthdate) {
        super(fullName, phoneNumber);
        this.username = username;
        this.password = password;
        this.birthdate = birthdate;
    }

   /*  @Override
    public String getRole() {
        return "Client";
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
} */