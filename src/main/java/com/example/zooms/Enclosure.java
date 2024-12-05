package com.example.zooms;

public class Enclosure {

    private String enclosureId;  // Auto-incremented
    private String name;
    private int capacity;
    private String type;

    // Constructor
    public Enclosure(String enclosureId, String name, int capacity, String type) {
        this.enclosureId = enclosureId;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
    }

    // Getters and Setters
    public String getEnclosureId() {
        return enclosureId;
    }

    public void setEnclosureId(String enclosureId) {
        this.enclosureId = enclosureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
