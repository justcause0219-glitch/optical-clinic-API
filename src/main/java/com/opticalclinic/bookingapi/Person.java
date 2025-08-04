package com.opticalclinic.bookingapi;

public class Person {
    protected String fullName;
    protected String phoneNumber;

    public Person(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    
    public String getFullName() {
        return fullName;
    }
  
public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getRole() {
        return "Person";
    }
}

