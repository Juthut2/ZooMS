package com.example.zooms;

public class AnimalCare {

    private String careId;
    private String animalId;
    private String employeeId;  // Added employeeId field
    private String careDate;  // Added careDate field
    private String careType;

    public AnimalCare(String careId, String animalId, String employeeId, String careDate, String careType) {
        this.careId = careId;
        this.animalId = animalId;
        this.employeeId = employeeId;
        this.careDate = careDate;
        this.careType = careType;
    }

    // Getters and Setters
    public String getCareId() {
        return careId;
    }

    public void setCareId(String careId) {
        this.careId = careId;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCareDate() {
        return careDate;
    }

    public void setCareDate(String careDate) {
        this.careDate = careDate;
    }

    public String getCareType() {
        return careType;
    }

    public void setCareType(String careType) {
        this.careType = careType;
    }
}
