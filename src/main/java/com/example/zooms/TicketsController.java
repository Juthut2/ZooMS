package com.example.zooms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TicketsController {

    @FXML
    private TextField TicketIDFeild, TicketPriceFeild, TicketValidityDateFeild, DateIssuedFeild;
    @FXML
    private ComboBox<String> TicketTypeFeild;
    @FXML
    private TableView<Ticket> TicketsTable;
    @FXML
    private AnchorPane TicketsPane;
    @FXML
    private Button AddBtn, UpdateBtn, DeleteBtn, ClearBtn;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();
        TicketTypeFeild.setItems(FXCollections.observableArrayList("Standard", "VIP", "Child", "Group"));
        loadTicketsTable();
    }

    @FXML
    void addTicket(ActionEvent event) {
        String query = "INSERT INTO Tickets (ticket_id, price, type, validity_date, date_issued) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, TicketIDFeild.getText());
            stmt.setDouble(2, Double.parseDouble(TicketPriceFeild.getText()));
            stmt.setString(3, TicketTypeFeild.getValue());
            stmt.setString(4, TicketValidityDateFeild.getText());
            stmt.setString(5, DateIssuedFeild.getText());
            stmt.executeUpdate();
            loadTicketsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add ticket: " + e.getMessage());
        }
    }

    @FXML
    void updateTicket(ActionEvent event) {
        String query = "UPDATE Tickets SET price = ?, type = ?, validity_date = ?, date_issued = ? WHERE ticket_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setDouble(1, Double.parseDouble(TicketPriceFeild.getText()));
            stmt.setString(2, TicketTypeFeild.getValue());
            stmt.setString(3, TicketValidityDateFeild.getText());
            stmt.setString(4, DateIssuedFeild.getText());
            stmt.setString(5, TicketIDFeild.getText());
            stmt.executeUpdate();
            loadTicketsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update ticket: " + e.getMessage());
        }
    }

    @FXML
    void deleteTicket(ActionEvent event) {
        String query = "DELETE FROM Tickets WHERE ticket_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, TicketIDFeild.getText());
            stmt.executeUpdate();
            loadTicketsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete ticket: " + e.getMessage());
        }
    }

    @FXML
    void clearFields() {
        TicketIDFeild.clear();
        TicketPriceFeild.clear();
        TicketTypeFeild.setValue(null);
        TicketValidityDateFeild.clear();
        DateIssuedFeild.clear();
    }

    private void loadTicketsTable() {
        ObservableList<Ticket> tickets = FXCollections.observableArrayList();
        String query = "SELECT * FROM Tickets";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tickets.add(new Ticket(
                        rs.getString("ticket_id"),
                        rs.getString("ticket_type"),
                        rs.getDouble("price"),  // Use getDouble for price
                        rs.getString("valid_from"),
                        rs.getString("valid_to")
                ));

            }
            TicketsTable.setItems(tickets);
        } catch (Exception e) {
            showAlert("Error", "Failed to load tickets: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }



@FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane,"AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane,"Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane,"Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane,"Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane,"Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane,"Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane,"Visitors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane,"HomePage.fxml");
    }
}
