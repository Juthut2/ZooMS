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

public class VisitorsController {

    @FXML
    private TextField VisitorsIDFeild, VisitorsFirstNameFeild, VisitorsLastNameFeild, VisitorsTicketIDFeild, VisitorStatusFeild, VisitorPaymentStatusFeild;
    @FXML
    private TableView<Visitor> VisitorsTable;
    @FXML
    private AnchorPane VisitorsPane;
    @FXML
    private Button AddBtn, UpdateBtn, DeleteBtn, ClearBtn;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();
        loadVisitorsTable();
    }

    @FXML
    void addVisitor(ActionEvent event) {
        String query = "INSERT INTO Visitors (visitor_id, first_name, last_name, ticket_id, status, payment_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, VisitorsIDFeild.getText());
            stmt.setString(2, VisitorsFirstNameFeild.getText());
            stmt.setString(3, VisitorsLastNameFeild.getText());
            stmt.setString(4, VisitorsTicketIDFeild.getText());
            stmt.setString(5, VisitorStatusFeild.getText());
            stmt.setString(6, VisitorPaymentStatusFeild.getText());
            stmt.executeUpdate();
            loadVisitorsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add visitor: " + e.getMessage());
        }
    }

    @FXML
    void updateVisitor(ActionEvent event) {
        String query = "UPDATE Visitors SET first_name = ?, last_name = ?, ticket_id = ?, status = ?, payment_status = ? WHERE visitor_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, VisitorsFirstNameFeild.getText());
            stmt.setString(2, VisitorsLastNameFeild.getText());
            stmt.setString(3, VisitorsTicketIDFeild.getText());
            stmt.setString(4, VisitorStatusFeild.getText());
            stmt.setString(5, VisitorPaymentStatusFeild.getText());
            stmt.setString(6, VisitorsIDFeild.getText());
            stmt.executeUpdate();
            loadVisitorsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update visitor: " + e.getMessage());
        }
    }

    @FXML
    void deleteVisitor(ActionEvent event) {
        String query = "DELETE FROM Visitors WHERE visitor_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, VisitorsIDFeild.getText());
            stmt.executeUpdate();
            loadVisitorsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete visitor: " + e.getMessage());
        }
    }

    @FXML
    void clearFields() {
        VisitorsIDFeild.clear();
        VisitorsFirstNameFeild.clear();
        VisitorsLastNameFeild.clear();
        VisitorsTicketIDFeild.clear();
        VisitorStatusFeild.clear();
        VisitorPaymentStatusFeild.clear();
    }

    private void loadVisitorsTable() {
        ObservableList<Visitor> visitors = FXCollections.observableArrayList();
        String query = "SELECT * FROM Visitors";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                visitors.add(new Visitor(
                        rs.getString("visitor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),  // Assuming you also retrieve email
                        rs.getString("phone"),  // Assuming you also retrieve phone
                        rs.getString("ticket_id"),
                        rs.getString("status"),
                        rs.getString("payment_status")
                ));

            }
            VisitorsTable.setItems(visitors);
        } catch (Exception e) {
            showAlert("Error", "Failed to load visitors: " + e.getMessage());
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
