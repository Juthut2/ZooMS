package com.example.zooms;

public class Event {
    private String eventId;
    private String animalName;
    private String employeeName;
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private String eventStatus;

    // Constructor
    public Event(String eventId, String animalName, String employeeName, String eventName, String eventDate, String eventLocation, String eventStatus) {
        this.eventId = eventId;
        this.animalName = animalName;
        this.employeeName = employeeName;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventStatus = eventStatus;
    }

    // Getters
    public String getEventId() {
        return eventId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    // Setters
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }
}
