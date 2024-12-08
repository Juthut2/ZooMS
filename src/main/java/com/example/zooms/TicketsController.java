package com.example.zooms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TicketsController {

    @FXML
    private Button AddBtn;

    @FXML
    private Button ClearBtn;

    @FXML
    private TextField DateIssuedFeild;

    @FXML
    private Button DeleteBtn;

    @FXML
    private TextField Event_IDFeild;

    @FXML
    private TableColumn<?, ?> ExpiryDateCell;

    @FXML
    private TableColumn<?, ?> Issue_Datecell;

    @FXML
    private TableColumn<?, ?> TicektEventname_Cell;

    @FXML
    private TextField TicketIDFeild;

    @FXML
    private TableColumn<?, ?> TicketID_Cell;

    @FXML
    private ComboBox<String> TicketTypeComboBox;

    @FXML
    private TableColumn<?, ?> TicketType_Cell;

    @FXML
    private TextField TicketValidityDateFeild;

    @FXML
    private TableColumn<?, ?> Ticket_PriceCell;

    @FXML
    private TextField Ticket_Price_Field;

    @FXML
    private AnchorPane TicketsPane;

    @FXML
    private TableView<Ticket> TicketsTable;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();
        TicketTypeComboBox.setItems(FXCollections.observableArrayList("Standard", "VIP", "Child", "Group", "Going To Event", "Visiting"));
        loadTicketsTable();

        // Set the CellValueFactory for each TableColumn
        TicketID_Cell.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        TicketType_Cell.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
        Ticket_PriceCell.setCellValueFactory(new PropertyValueFactory<>("ticketPrice"));
        Issue_Datecell.setCellValueFactory(new PropertyValueFactory<>("dateIssued"));
        ExpiryDateCell.setCellValueFactory(new PropertyValueFactory<>("validityDate"));
        TicektEventname_Cell.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        // Disable Event_IDFeild by default
        Event_IDFeild.setDisable(true);

        // Enable or disable Event_IDFeild based on the selected TicketType
        TicketTypeComboBox.setOnAction(e -> handleTicketTypeSelection());

        TicketsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && TicketsTable.getSelectionModel().getSelectedItem() != null) {
                populateFields(TicketsTable.getSelectionModel().getSelectedItem());
            }
        });
    }


    private void handleTicketTypeSelection() {
        if ("Going To Event".equals(TicketTypeComboBox.getValue())) {
            Event_IDFeild.setDisable(false);  // Enable Event_IDFeild if "Going To Event" is selected
            Event_IDFeild.setText("");         // Clear the Event_IDFeild when it's enabled
        } else {
            Event_IDFeild.setDisable(true);   // Disable Event_IDFeild if "Visiting" or other types are selected
            Event_IDFeild.setText("0");       // Set Event ID to a dummy value (e.g., "0") when ticket type is not for an event
        }
    }


    @FXML
    void addTicket(ActionEvent event) {
        String query = "INSERT INTO Tickets (ticket_price, ticket_type, valid_until, issue_date, event_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setDouble(1, Double.parseDouble(Ticket_Price_Field.getText()));
            stmt.setString(2, TicketTypeComboBox.getValue());
            stmt.setString(3, TicketValidityDateFeild.getText());
            stmt.setString(4, DateIssuedFeild.getText());

            // If the ticket is "Going To Event", set the actual event_id, otherwise use a dummy value
            if ("Going To Event".equals(TicketTypeComboBox.getValue())) {
                stmt.setString(5, Event_IDFeild.getText());
            } else {
                stmt.setInt(5, 0);  // Use -1 or 0 for the dummy event ID
            }

            stmt.executeUpdate();
            loadTicketsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add ticket: " + e.getMessage());
        }
    }


    @FXML
    void updateTicket(ActionEvent event) {
        String query = "UPDATE Tickets SET ticket_price = ?, ticket_type = ?, valid_until = ?, issue_date = ?, event_id = ? WHERE ticket_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setDouble(1, Double.parseDouble(Ticket_Price_Field.getText()));
            stmt.setString(2, TicketTypeComboBox.getValue());
            stmt.setString(3, TicketValidityDateFeild.getText());
            stmt.setString(4, DateIssuedFeild.getText());

            // Handle event_id based on ticket type
            if ("Going To Event".equals(TicketTypeComboBox.getValue())) {
                stmt.setString(5, Event_IDFeild.getText()); // Use the entered event ID
            } else {
                stmt.setInt(5, -1);  // Use -1 (or another dummy value) for non-event tickets
            }

            stmt.setString(6, TicketIDFeild.getText());  // Update the ticket based on its ticket_id
            stmt.executeUpdate();
            loadTicketsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update ticket: " + e.getMessage());
        }
    }


    @FXML
    void deleteTicket(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Ticket");
        dialog.setHeaderText("Enter the Ticket ID to delete:");
        dialog.setContentText("Ticket ID:");

        dialog.showAndWait().ifPresent(ticketId -> {
            try {
                String query = "DELETE FROM Tickets WHERE ticket_id = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, ticketId);
                stmt.executeUpdate();
                loadTicketsTable();
                clearFields();
            } catch (Exception e) {
                showAlert("Error", "Failed to delete ticket: " + e.getMessage());
            }
        });
    }

    @FXML
    void clearFields() {
        TicketIDFeild.clear();
        Ticket_Price_Field.clear();
        TicketTypeComboBox.setValue(null);
        TicketValidityDateFeild.clear();
        DateIssuedFeild.clear();
        Event_IDFeild.clear();
    }

    private void loadTicketsTable() {
        ObservableList<Ticket> tickets = FXCollections.observableArrayList();
        String query = "SELECT t.ticket_id, t.ticket_type, t.ticket_price, t.issue_date, t.valid_until, " +
                "e.event_name FROM Tickets t LEFT JOIN ZooEvents e ON t.event_id = e.event_id";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tickets.add(new Ticket(
                        rs.getString("ticket_id"),
                        rs.getString("ticket_type"),
                        rs.getDouble("ticket_price"),
                        rs.getString("issue_date"),
                        rs.getString("valid_until"),
                        rs.getString("event_name")  // Display event name instead of event ID
                ));
            }
            TicketsTable.setItems(tickets);
        } catch (Exception e) {
            showAlert("Error", "Failed to load tickets: " + e.getMessage());
        }
    }

    private void populateFields(Ticket ticket) {
        TicketIDFeild.setText(ticket.getTicketId());
        Ticket_Price_Field.setText(String.valueOf(ticket.getTicketPrice()));
        TicketTypeComboBox.setValue(ticket.getTicketType());
        TicketValidityDateFeild.setText(ticket.getValidityDate());
        DateIssuedFeild.setText(ticket.getDateIssued());
        Event_IDFeild.setText(ticket.getEventName() == null ? "N/A" : ticket.getEventName());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane, "AnimalCare.fxml");
    }

    // Go to Animals (this can be left empty or include specific logic if needed)
    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane, "Animals.fxml");
    }

    // Go to Employees
    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane, "Employees.fxml");
    }

    // Go to Enclosures
    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane, "Enclosures.fxml");
    }

    // Go to Events
    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane, "Events.fxml");
    }

    // Go to Tickets
    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane, "Tickets.fxml");
    }

    // Go to Visitors
    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane, "Visitors.fxml");
    }

    // Go to HomePage
    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(TicketsPane , "HomePage.fxml");

    }
}
