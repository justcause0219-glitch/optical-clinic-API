package com.opticalclinic.bookingapi;
public class Employee extends Person {
    private String employeeID;
    

    public Employee (String fullName, String phoneNumber, String employeeID) {
        super(fullName, phoneNumber);
        this.employeeID = employeeID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

   /*  @Override
    public String getRole() {
        return "Employee";
    }
*/
}
