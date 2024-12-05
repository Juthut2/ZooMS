package com.example.zooms;

public class Ticket {

    private String ticketId;
    private String ticketType;
    private double ticketPrice;  // Change to double for numeric handling
    private String validFrom;
    private String validTo;

    public Ticket(String ticketId, String ticketType, double ticketPrice, String validFrom, String validTo) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;  // Use double for price
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getTicketPrice() {
        return ticketPrice;  // Return as double
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;  // Accept double as input
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
}
