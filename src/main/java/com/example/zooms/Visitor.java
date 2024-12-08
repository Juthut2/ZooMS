package com.example.zooms;

public class Visitor {

    private String visitorId;
    private String firstName;
    private String lastName;
    private String ticketId;
    private String paymentStatus;

    // Constructor
    public Visitor(String visitorId, String firstName, String lastName, String ticketId, String paymentStatus) {
        this.visitorId = visitorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ticketId = ticketId;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
