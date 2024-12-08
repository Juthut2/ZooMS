package com.example.zooms;

public class Ticket {

    private String ticketId;
    private String ticketType;
    private double ticketPrice;
    private String dateIssued;
    private String validityDate;
    private String eventName;  // This will hold the event name when displaying in the table

    // Constructor
    public Ticket(String ticketId, String ticketType, double ticketPrice, String dateIssued, String validityDate, String eventName) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
        this.dateIssued = dateIssued;
        this.validityDate = validityDate;
        this.eventName = eventName;
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
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", ticketType='" + ticketType + '\'' +
                ", ticketPrice=" + ticketPrice +
                ", dateIssued='" + dateIssued + '\'' +
                ", validityDate='" + validityDate + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
