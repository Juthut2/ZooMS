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

public class EventsController {

    @FXML
    private Button AddBtn;

    @FXML
    private TextField Animal_IDFeild;

    @FXML
    private TableColumn<?, ?> Animal_ID_Cell;

    @FXML
    private Button ClearBtn;

    @FXML
    private Button DeleteBtn;

    @FXML
    private TextField EventIDFeild;

    @FXML
    private TextField EmployeeIDFeild;

    @FXML
    private TableColumn<?, ?> Employee_ID_cell;

    @FXML
    private TextField EventDateFeild;

    @FXML
    private TextField EventLocationFeild;

    @FXML
    private TableColumn<?, ?> EventName_cell;

    @FXML
    private ComboBox<String> EventStatus;

    @FXML
    private TableColumn<?, ?> Event_Date_cell;

    @FXML
    private TableColumn<?, ?> Event_ID_Cell;

    @FXML
    private TextField Event_NameFeild;

    @FXML
    private TableColumn<?, ?> Event_Status_Cell;

    @FXML
    private TableColumn<?, ?> Event_location_Cell;

    @FXML
    private AnchorPane EventsPane;

    @FXML
    private TableView<Event> EventsTable;

    @FXML
    private Button UpdateBtn;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();
        EventStatus.setItems(FXCollections.observableArrayList("Upcoming", "Ongoing", "Closed"));

        // Set cell value factories for table columns
        Event_ID_Cell.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        Animal_ID_Cell.setCellValueFactory(new PropertyValueFactory<>("animalName"));
        Employee_ID_cell.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        EventName_cell.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        Event_Date_cell.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        Event_location_Cell.setCellValueFactory(new PropertyValueFactory<>("eventLocation"));
        Event_Status_Cell.setCellValueFactory(new PropertyValueFactory<>("eventStatus"));

        // Load table data
        loadEventsTable();

        // Add double-click functionality
        EventsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && EventsTable.getSelectionModel().getSelectedItem() != null) {
                populateFields(EventsTable.getSelectionModel().getSelectedItem());
            }
        });
    }


    @FXML
    void addEvent(ActionEvent event) {
        try {
            // Validate Employee Role
            String employeeId = EmployeeIDFeild.getText();
            if (!isEventManager(employeeId)) {
                showAlert("Error", "Only employees with the 'Event Manager' role can be assigned to events.");
                return;
            }

            // Insert into ZooEvents
            String query = "INSERT INTO ZooEvents (animal_id, employee_id, event_name, event_date, event_location, event_status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, Animal_IDFeild.getText());
            stmt.setString(2, employeeId);
            stmt.setString(3, Event_NameFeild.getText());
            stmt.setString(4, EventDateFeild.getText());
            stmt.setString(5, EventLocationFeild.getText());
            stmt.setString(6, EventStatus.getValue());
            stmt.executeUpdate();

            loadEventsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add event: " + e.getMessage());
        }
    }

    @FXML
    void updateEvent(ActionEvent event) {
        try {
            String query = "UPDATE ZooEvents SET animal_id = ?, employee_id = ?, event_name = ?, event_date = ?, event_location = ?, event_status = ? WHERE event_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, Animal_IDFeild.getText());
            stmt.setString(2, EmployeeIDFeild.getText());
            stmt.setString(3, Event_NameFeild.getText());
            stmt.setString(4, EventDateFeild.getText());
            stmt.setString(5, EventLocationFeild.getText());
            stmt.setString(6, EventStatus.getValue());
            stmt.setString(7, EventIDFeild.getText());
            stmt.executeUpdate();

            loadEventsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update event: " + e.getMessage());
        }
    }

    @FXML
    void deleteEvent(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Event");
        dialog.setHeaderText("Enter the Event ID to delete:");
        dialog.setContentText("Event ID:");

        dialog.showAndWait().ifPresent(eventId -> {
            try {
                String query = "DELETE FROM ZooEvents WHERE event_id = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, eventId);
                stmt.executeUpdate();

                loadEventsTable();
                clearFields();
            } catch (Exception e) {
                showAlert("Error", "Failed to delete event: " + e.getMessage());
            }
        });
    }

    private void loadEventsTable() {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String query = """
                SELECT ze.event_id, ze.event_name, ze.event_date, ze.event_location, ze.event_status,
                       a.animal_name AS animal_name, CONCAT(e.first_name, ' ', e.last_name) AS employee_name
                FROM ZooEvents ze
                JOIN Animals a ON ze.animal_id = a.animal_id
                JOIN Employees e ON ze.employee_id = e.employee_id
                """;
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getString("event_id"),
                        rs.getString("animal_name"),
                        rs.getString("employee_name"),
                        rs.getString("event_name"),
                        rs.getString("event_date"),
                        rs.getString("event_location"),
                        rs.getString("event_status")
                ));
            }
            EventsTable.setItems(events);
        } catch (Exception e) {
            showAlert("Error", "Failed to load events: " + e.getMessage());
        }
    }

    private boolean isEventManager(String employeeId) {
        String query = "SELECT r.role_name FROM Employees e JOIN Roles r ON e.role_id = r.role_id WHERE e.employee_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && "Event Manager".equalsIgnoreCase(rs.getString("role_name")) ;
        } catch (Exception e) {
            showAlert("Error", "Failed to validate employee role: " + e.getMessage());
            return false;
        }
    }

    @FXML
    private void clearFields() {
        EventIDFeild.clear();
        Animal_IDFeild.clear();
        EmployeeIDFeild.clear();
        Event_NameFeild.clear();
        EventDateFeild.clear();
        EventLocationFeild.clear();
        EventStatus.setValue(null);
    }

    private void populateFields(Event event) {
        EventIDFeild.setText(event.getEventId());
        Animal_IDFeild.setText(event.getAnimalName());
        EmployeeIDFeild.setText(event.getEmployeeName());
        Event_NameFeild.setText(event.getEventName());
        EventDateFeild.setText(event.getEventDate());
        EventLocationFeild.setText(event.getEventLocation());
        EventStatus.setValue(event.getEventStatus());
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

    // Go to Animals (this can be left empty or include specific logic if needed)
    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Animals.fxml");
    }

    // Go to Employees
    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Employees.fxml");
    }

    // Go to Enclosures
    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Enclosures.fxml");
    }

    // Go to Events
    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Events.fxml");
    }

    // Go to Tickets
    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Tickets.fxml");
    }

    // Go to Visitors
    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "Visitors.fxml");
    }

    // Go to HomePage
    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(EventsPane, "HomePage.fxml");

    }
}
