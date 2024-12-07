package com.example.zooms;

public class Employee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String roleName;
    private double salary;
    private String dateHired;

    public Employee(String employeeId, String firstName, String lastName, String roleName, double salary, String dateHired) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleName = roleName;
        this.salary = salary;
        this.dateHired = dateHired;
    }

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDateHired() {
        return dateHired;
    }

    public void setDateHired(String dateHired) {
        this.dateHired = dateHired;
    }
}
