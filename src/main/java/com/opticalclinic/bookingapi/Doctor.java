package com.opticalclinic.bookingapi;

public class Doctor extends Person {
    private String doctorID;
    private String specialization;

    public Doctor(String fullName, String phoneNumber, int id, String doctorID, String specialization) {
        super(fullName, phoneNumber);
        this.doctorID = doctorID;
        this.specialization = specialization;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /*
    @Override
    public String getRole() {
        return "Doctor";
    }
    */
}
