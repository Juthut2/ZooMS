package com.example.zooms;

public class AnimalCare {
    private String careId;
    private String animalId;  // This will hold the animal ID
    private String employeeId; // This will hold the employee ID
    private String careDate;
    private String careType;
    private String animalName; // This will hold the animal name
    private String employeeName; // This will hold the employee's full name

    // Constructor
    public AnimalCare(String careId, String animalName, String employeeName, String careDate, String careType) {
        this.careId = careId;
        this.animalName = animalName;
        this.employeeName = employeeName;
        this.careDate = careDate;
        this.careType = careType;
        // Add logic to fetch the corresponding IDs as needed
    }

    // Getters for IDs
    public String getCareId() { return careId; }
    public String getAnimalId() { return animalId; }  // Getter for the Animal ID
    public String getEmployeeId() { return employeeId; } // Getter for Employee ID
    public String getCareDate() { return careDate; }
    public String getCareType() { return careType; }

    // Getters for names (to be displayed in the table)
    public String getAnimalName() { return animalName; }
    public String getEmployeeName() { return employeeName; }
}

