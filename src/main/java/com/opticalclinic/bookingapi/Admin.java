package com.opticalclinic.bookingapi;

public class Admin extends Person {
    private String adminID;

    public Admin(String fullName, String phoneNumber, int id, String adminID) {
        super(fullName, phoneNumber);
        this.adminID = adminID;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
/*
    @Override
    public String getRole() {
        return "Admin";
    }
*/
}
