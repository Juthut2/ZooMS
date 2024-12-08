package com.example.zooms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VisitorsController {

    @FXML
    private Button AddBtn, ClearBtn, DeleteBtn, PaymentBtn, UpdateBtn, goToCareType;

    @FXML
    private TableColumn<Visitor, String> Visitor_IDCell, Ticket_IDCell, VisitorS_firstNameCell, Visitor_lastNameCell, Payment_statusCell;

    @FXML
    private TextField VisitorsIDFeild, VisitorsFirstNameFeild, VisitorsLastNameFeild, VisitorsTicketIDFeild, VisitorPaymentStatusFeild;

    @FXML
    private TableView<Visitor> VisitorsTable;

    @FXML
    private AnchorPane VisitorsPane;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();
        loadVisitorsTable();

        // Bind table columns to Visitor class properties
        Visitor_IDCell.setCellValueFactory(new PropertyValueFactory<>("visitorId"));
        VisitorS_firstNameCell.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        Visitor_lastNameCell.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        Ticket_IDCell.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        Payment_statusCell.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        // Double-click functionality to populate fields for editing
        VisitorsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && VisitorsTable.getSelectionModel().getSelectedItem() != null) {
                Visitor selectedVisitor = VisitorsTable.getSelectionModel().getSelectedItem();
                populateFields(selectedVisitor);
            }
        });

        loadVisitorsTable(); // Ensure table is loaded after bindings
    }


    @FXML
    void addVisitor(ActionEvent event) {
        String query = "INSERT INTO Visitors (first_name, last_name, ticket_id, payment_status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, VisitorsFirstNameFeild.getText());
            stmt.setString(2, VisitorsLastNameFeild.getText());
            stmt.setString(3, VisitorsTicketIDFeild.getText());
            stmt.setString(4, VisitorPaymentStatusFeild.getText());
            stmt.executeUpdate();
            loadVisitorsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add visitor: " + e.getMessage());
        }
    }

    @FXML
    void updateVisitor(ActionEvent event) {
        String query = "UPDATE Visitors SET first_name = ?, last_name = ?, ticket_id = ?, payment_status = ? WHERE visitor_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, VisitorsFirstNameFeild.getText());
            stmt.setString(2, VisitorsLastNameFeild.getText());
            stmt.setString(3, VisitorsTicketIDFeild.getText());
            stmt.setString(4, VisitorPaymentStatusFeild.getText());
            stmt.setString(5, VisitorsIDFeild.getText());
            stmt.executeUpdate();
            loadVisitorsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update visitor: " + e.getMessage());
        }
    }

    @FXML
    void deleteVisitor(ActionEvent event) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Delete Visitor");
        inputDialog.setHeaderText("Enter Visitor ID to delete:");
        inputDialog.setContentText("Visitor ID:");

        inputDialog.showAndWait().ifPresent(visitorId -> {
            String query = "DELETE FROM Visitors WHERE visitor_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, visitorId);
                stmt.executeUpdate();
                loadVisitorsTable();
                clearFields();
            } catch (Exception e) {
                showAlert("Error", "Failed to delete visitor: " + e.getMessage());
            }
        });
    }

    @FXML
    void handlePayment(ActionEvent event) {
        TextInputDialog paymentDialog = new TextInputDialog();
        paymentDialog.setTitle("Process Payment");
        paymentDialog.setHeaderText("Enter Visitor ID for Payment:");
        paymentDialog.setContentText("Visitor ID:");

        paymentDialog.showAndWait().ifPresent(visitorId -> {
            String getTicketPriceQuery = "SELECT ticket_price FROM Tickets WHERE ticket_id = (SELECT ticket_id FROM Visitors WHERE visitor_id = ?)";
            String updatePaymentQuery = "UPDATE Visitors SET payment_status = 'Paid' WHERE visitor_id = ?";

            try (PreparedStatement getPriceStmt = con.prepareStatement(getTicketPriceQuery)) {
                // Retrieve the ticket price
                getPriceStmt.setString(1, visitorId);
                ResultSet rs = getPriceStmt.executeQuery();
                if (rs.next()) {
                    double ticketPrice = rs.getDouble("ticket_price");

                    // Ask for payment
                    TextInputDialog amountDialog = new TextInputDialog();
                    amountDialog.setTitle("Enter Payment");
                    amountDialog.setHeaderText("Ticket Price: " + ticketPrice);
                    amountDialog.setContentText("Enter payment amount:");

                    amountDialog.showAndWait().ifPresent(amount -> {
                        try (PreparedStatement updateStmt = con.prepareStatement(updatePaymentQuery)) {
                            double payment = Double.parseDouble(amount);
                            if (payment >= ticketPrice) {
                                double change = payment - ticketPrice;
                                showAlert("Payment Success", "Payment accepted. Change: " + change);

                                // Update payment status
                                updateStmt.setString(1, visitorId);
                                updateStmt.executeUpdate();

                                // Print ticket
                                printTicket(visitorId);

                                loadVisitorsTable();
                            } else {
                                showAlert("Payment Failed", "Insufficient payment. Please try again.");
                            }
                        } catch (Exception e) {
                            showAlert("Error", "Failed to update payment status: " + e.getMessage());
                        }
                    });
                } else {
                    showAlert("Error", "No ticket found for the visitor.");
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to process payment: " + e.getMessage());
            }
        });
    }


    private void printTicket(String visitorId) {
        String query = "SELECT ticket_id, ticket_type, " +
                "(SELECT event_name FROM zooevents WHERE zooevents.event_id = Tickets.event_id) AS event_name " +
                "FROM Tickets WHERE ticket_id = (SELECT ticket_id FROM Visitors WHERE visitor_id = ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, visitorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String ticketId = rs.getString("ticket_id");
                String ticketType = rs.getString("ticket_type");
                String eventName = rs.getString("event_name");

                // Ticket design
                StringBuilder ticketContent = new StringBuilder();
                ticketContent.append("============================\n");
                ticketContent.append("         MOODENG ZOO         \n");
                ticketContent.append("============================\n");
                ticketContent.append("Ticket ID: ").append(ticketId).append("\n");
                ticketContent.append("Ticket Type: ").append(ticketType).append("\n");

                if (eventName != null && !eventName.isEmpty()) {
                    ticketContent.append("Event: ").append(eventName).append("\n");
                }
                if (ticketType.equalsIgnoreCase("VIP")) {
                    ticketContent.append("Access: Exclusive VIP Areas\n");
                }
                ticketContent.append("============================\n");
                ticketContent.append("Thank you for visiting!\n");

                // Save as .txt file
                saveAsTextFile(ticketId, ticketContent.toString());

                // Save as .pdf file (optional)


                showAlert("Ticket Saved", "The ticket has been saved as both .txt and .pdf files.");
            } else {
                showAlert("Error", "Ticket details not found.");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to print ticket: " + e.getMessage());
        }
    }

    private void saveAsTextFile(String ticketId, String content) {
        try (FileWriter writer = new FileWriter(ticketId + "_Ticket.txt")) {
            writer.write(content);
        } catch (IOException e) {
            showAlert("Error", "Failed to save ticket as .txt file: " + e.getMessage());
        }
    }




    private void loadVisitorsTable() {
        ObservableList<Visitor> visitors = FXCollections.observableArrayList();
        String query = "SELECT visitor_id, first_name, last_name, ticket_id, payment_status FROM Visitors";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                visitors.add(new Visitor(
                        rs.getString("visitor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("ticket_id"),
                        rs.getString("payment_status")
                ));
            }
            VisitorsTable.setItems(visitors);
        } catch (Exception e) {
            showAlert("Error", "Failed to load visitors: " + e.getMessage());
        }
    }

    private void populateFields(Visitor visitor) {
        VisitorsIDFeild.setText(visitor.getVisitorId());
        VisitorsFirstNameFeild.setText(visitor.getFirstName());
        VisitorsLastNameFeild.setText(visitor.getLastName());
        VisitorsTicketIDFeild.setText(visitor.getTicketId());
        VisitorPaymentStatusFeild.setText(visitor.getPaymentStatus());
    }

    @FXML
    private void clearFields() {
        VisitorsIDFeild.clear();
        VisitorsFirstNameFeild.clear();
        VisitorsLastNameFeild.clear();
        VisitorsTicketIDFeild.clear();
        VisitorPaymentStatusFeild.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }





@FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(VisitorsPane,"AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(VisitorsPane,"Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(VisitorsPane,"Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(VisitorsPane,"Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(VisitorsPane,"Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(VisitorsPane,"Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(VisitorsPane,"Visitors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(VisitorsPane,"HomePage.fxml");
    }

}
