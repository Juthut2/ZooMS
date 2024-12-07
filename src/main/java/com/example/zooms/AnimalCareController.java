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

public class AnimalCareController {

    @FXML
    private Button AddBtn;

    @FXML
    private AnchorPane AnimalCarePane;

    @FXML
    private TableView<AnimalCare> AnimalCareTable;

    @FXML
    private TableColumn<AnimalCare, String> Animal_ID;

    @FXML
    private TextField Animal_IDFeild;

    @FXML
    private TableColumn<AnimalCare, String> CareDate;

    @FXML
    private TextField CareDateFeild;

    @FXML
    private TextField CareIDFeild;

    @FXML
    private TextField CareTypeFeild;

    @FXML
    private TableColumn<AnimalCare, String> Care_ID;

    @FXML
    private TableColumn<AnimalCare, String> Care_Type;

    @FXML
    private Button ClearBtn;

    @FXML
    private Button DeleteBtn;

    @FXML
    private TextField EmployeeIDFeild;

    @FXML
    private TableColumn<AnimalCare, String> Employee_ID;

    @FXML
    private Button UpdateBtn;

    @FXML
    private AnchorPane animalCare_form;

    @FXML
    private Button goToCareType;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();

        // Set the CellValueFactory for each column to extract data from the AnimalCare object
        Animal_ID.setCellValueFactory(new PropertyValueFactory<>("animalName"));  // Display name in the table
        CareDate.setCellValueFactory(new PropertyValueFactory<>("careDate"));
        Care_ID.setCellValueFactory(new PropertyValueFactory<>("careId"));
        Care_Type.setCellValueFactory(new PropertyValueFactory<>("careType"));
        Employee_ID.setCellValueFactory(new PropertyValueFactory<>("employeeName")); // Display employee name

        loadAnimalCareTable();

        // Add a listener to detect double-click on a row in the TableView
        AnimalCareTable.setRowFactory(tv -> {
            TableRow<AnimalCare> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) { // Double-click detected
                    AnimalCare selectedAnimalCare = row.getItem();
                    populateFields(selectedAnimalCare);  // Populate fields with IDs
                }
            });
            return row;
        });
    }



    @FXML
    void addAnimalCare(ActionEvent event) {
        String employeeId = EmployeeIDFeild.getText();
        String query = "SELECT r.role_name FROM Employees e JOIN Roles r ON e.role_id = r.role_id WHERE e.employee_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String roleName = rs.getString("role_name");
                if (!roleName.equals("Caretaker")) {
                    showAlert("Role Error", "Only employees with the 'Caretaker' role can be assigned to animal care.");
                    return;
                }
            } else {
                showAlert("Employee Error", "Employee not found.");
                return;
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to check employee role: " + e.getMessage());
            return;
        }

        // Proceed with adding animal care if the employee is a caretaker
        String insertQuery = "INSERT INTO AnimalCare (care_id, animal_id, employee_id, care_date, care_type_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
            stmt.setString(1, CareIDFeild.getText());
            stmt.setString(2, Animal_IDFeild.getText());
            stmt.setString(3, EmployeeIDFeild.getText());
            stmt.setString(4, CareDateFeild.getText());
            stmt.setString(5, CareTypeFeild.getText());
            stmt.executeUpdate();
            loadAnimalCareTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add animal care: " + e.getMessage());
        }
    }



    @FXML
    void updateAnimalCare(ActionEvent event) {
        String query = "UPDATE AnimalCare SET animal_id = ?, employee_id = ?, care_date = ?, care_type_id = ? WHERE care_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, Animal_IDFeild.getText());
            stmt.setString(2, EmployeeIDFeild.getText());
            stmt.setString(3, CareDateFeild.getText());
            stmt.setString(4, CareTypeFeild.getText());
            stmt.setString(5, CareIDFeild.getText());
            stmt.executeUpdate();
            loadAnimalCareTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update animal care: " + e.getMessage());
        }
    }

    private void populateFields(AnimalCare animalCare) {
        // Set the text fields with the ID values corresponding to the selected row
        CareIDFeild.setText(animalCare.getCareId());        // Set the Care ID
        Animal_IDFeild.setText(animalCare.getAnimalId());    // Set the Animal ID (not name)
        EmployeeIDFeild.setText(animalCare.getEmployeeId()); // Set the Employee ID (not name)
        CareDateFeild.setText(animalCare.getCareDate());     // Set the Care Date
        CareTypeFeild.setText(animalCare.getCareType());     // Set the Care Type ID (not name)
    }


    @FXML
    void deleteAnimalCare(ActionEvent event) {
        // Prompt the user to enter the Care ID for deletion
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Animal Care");
        dialog.setHeaderText("Enter Care ID to delete:");
        dialog.setContentText("Care ID:");

        // Wait for user input
        dialog.showAndWait().ifPresent(careId -> {
            // Proceed with deletion if the Care ID is entered
            String query = "DELETE FROM AnimalCare WHERE care_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, careId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    loadAnimalCareTable();
                    clearFields();
                    showAlert("Success", "Animal care with Care ID " + careId + " has been deleted.");
                } else {
                    showAlert("Error", "No record found with the given Care ID.");
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to delete animal care: " + e.getMessage());
            }
        });
    }


    @FXML
    void clearFields() {
        CareIDFeild.clear();
        Animal_IDFeild.clear();
        EmployeeIDFeild.clear();
        CareDateFeild.clear();
        CareTypeFeild.clear();
    }

    private void loadAnimalCareTable() {
        ObservableList<AnimalCare> animalCareList = FXCollections.observableArrayList();
        String query = """
                SELECT ac.care_id, ac.animal_id, ac.employee_id, ac.care_date, ct.care_type_name, a.animal_name, e.first_name, e.last_name
                FROM AnimalCare ac
                JOIN CareType ct ON ac.care_type_id = ct.care_type_id
                JOIN Animals a ON ac.animal_id = a.animal_id
                JOIN Employees e ON ac.employee_id = e.employee_id
                """;
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String employeeFullName = rs.getString("first_name") + " " + rs.getString("last_name");
                animalCareList.add(new AnimalCare(
                        rs.getString("care_id"),
                        rs.getString("animal_name"), // Display animal name
                        employeeFullName, // Display employee full name
                        rs.getString("care_date"),
                        rs.getString("care_type_name") // Display care type name
                ));
            }
            AnimalCareTable.setItems(animalCareList);
        } catch (Exception e) {
            showAlert("Error", "Failed to load animal care: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "Visitors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "HomePage.fxml");
    }

    @FXML
    void goToCareType(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "CareType.fxml");
    }
    @FXML
    void goAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "CareType.fxml");
    }
}
