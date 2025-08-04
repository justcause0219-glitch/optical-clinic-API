package com.opticalclinic.bookingapi;

public class Booking {
    private String fullName;
    private String age;
    private String contact;
    private String doctor;
    private String time;
    private String date;

    public Booking(String fullName, String age, String contact, String doctor, String time, String date) {
        this.fullName = fullName;
        this.age = age;
        this.contact = contact;
        this.doctor = doctor;
        this.time = time;
        this.date = date;
    } 

    // Getters
    public String getFullName() { return fullName; }
    public String getAge() { return age; }
    public String getContact() { return contact; }
    public String getDoctor() { return doctor; }
    public String getTime() { return time; }
    public String getDate() { return date; }
}
