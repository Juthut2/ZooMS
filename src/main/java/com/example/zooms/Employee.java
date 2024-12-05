package com.example.zooms;

public class Employee {

    private String employeeId;
    private String firstName;
    private String lastName;
    private String role;
    private double salary;  // Salary as double
    private String dateHired;  // Added dateHired as a String (or Date if preferred)

    // Constructor with dateHired added
    public Employee(String employeeId, String firstName, String lastName, String role, double salary, String dateHired) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.salary = salary;
        this.dateHired = dateHired;  // Initialize dateHired
    }

    // Getters and Setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;  // Return as double
    }

    public void setSalary(double salary) {
        this.salary = salary;  // Accept double as input
    }

    public String getDateHired() {
        return dateHired;  // Return dateHired as a String (or Date if using java.util.Date)
    }

    public void setDateHired(String dateHired) {
        this.dateHired = dateHired;  // Accept dateHired as a String (or Date if using java.util.Date)
    }
}
