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

public class EventsController {

    @FXML
    private TextField EventIDFeild, Event_Name, EventDateFeild, EventLocationFeild;
    @FXML
    private ComboBox<String> EventStatus;
    @FXML
    private TableView<Event> EventsTable;
    @FXML
    private AnchorPane EventsPane;
    @FXML
    private Button AddBtn, UpdateBtn, DeleteBtn, ClearBtn;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();
        EventStatus.setItems(FXCollections.observableArrayList("Scheduled", "Ongoing", "Completed", "Cancelled"));
        loadEventsTable();
    }

    @FXML
    void addEvent(ActionEvent event) {
        String query = "INSERT INTO ZooEvents (event_id, name, date, location, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, EventIDFeild.getText());
            stmt.setString(2, Event_Name.getText());
            stmt.setString(3, EventDateFeild.getText());
            stmt.setString(4, EventLocationFeild.getText());
            stmt.setString(5, EventStatus.getValue());
            stmt.executeUpdate();
            loadEventsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add event: " + e.getMessage());
        }
    }

    @FXML
    void updateEvent(ActionEvent event) {
        String query = "UPDATE ZooEvents SET name = ?, date = ?, location = ?, status = ? WHERE event_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, Event_Name.getText());
            stmt.setString(2, EventDateFeild.getText());
            stmt.setString(3, EventLocationFeild.getText());
            stmt.setString(4, EventStatus.getValue());
            stmt.setString(5, EventIDFeild.getText());
            stmt.executeUpdate();
            loadEventsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update event: " + e.getMessage());
        }
    }

    @FXML
    void deleteEvent(ActionEvent event) {
        String query = "DELETE FROM ZooEvents WHERE event_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, EventIDFeild.getText());
            stmt.executeUpdate();
            loadEventsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete event: " + e.getMessage());
        }
    }

    @FXML
    void clearFields() {
        EventIDFeild.clear();
        Event_Name.clear();
        EventDateFeild.clear();
        EventLocationFeild.clear();
        EventStatus.setValue(null);
    }

    private void loadEventsTable() {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String query = "SELECT * FROM ZooEvents";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getString("event_id"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getString("status")
                ));
            }
            EventsTable.setItems(events);
        } catch (Exception e) {
            showAlert("Error", "Failed to load events: " + e.getMessage());
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
        new SceneSwitch(EventsPane, "AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Visitors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane,"HomePage.fxml");
    }
}
