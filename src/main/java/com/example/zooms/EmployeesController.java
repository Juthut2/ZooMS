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
import java.sql.SQLException;

public class EmployeesController {

    @FXML
    private TableView<Employee> EmployeeTable;

    @FXML
    private TableColumn<Employee, String> Employee_IDCell, First_NameCell, Last_NameCell, Role_Cell;

    @FXML
    private TableColumn<Employee, Double> Salary_cell;

    @FXML
    private TableColumn<Employee, String> Date_hired_Cell;

    @FXML
    private TextField EmployeeFeild, EmployeeFirstNameFeild, EmployeeLastNameFeild, EmployeeRoleFeild, EmployeeDateHiredFeild;

    @FXML
    private Button AddBtn, UpdateBtn, DeleteBtn, ClearBtn;

    @FXML
    private AnchorPane EmployeesPane;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();

        // Configure table columns
        Employee_IDCell.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        First_NameCell.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        Last_NameCell.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        Role_Cell.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        Salary_cell.setCellValueFactory(new PropertyValueFactory<>("salary"));
        Date_hired_Cell.setCellValueFactory(new PropertyValueFactory<>("dateHired"));

        loadEmployeeTable();

        // Double-click to populate fields
        EmployeeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Employee selectedEmployee = EmployeeTable.getSelectionModel().getSelectedItem();
                if (selectedEmployee != null) {
                    EmployeeFeild.setText(selectedEmployee.getEmployeeId());
                    EmployeeFirstNameFeild.setText(selectedEmployee.getFirstName());
                    EmployeeLastNameFeild.setText(selectedEmployee.getLastName());
                    EmployeeRoleFeild.setText(getRoleIdFromRoleName(selectedEmployee.getRoleName()));
                    EmployeeDateHiredFeild.setText(selectedEmployee.getDateHired());
                }
            }
        });
    }

    @FXML
    void addEmployee(ActionEvent event) {
        String roleId = EmployeeRoleFeild.getText();

        // Validate Role ID
        if (!isValidRole(roleId)) {
            showAlert("Error", "Invalid Role ID!");
            return;
        }

        String query = "INSERT INTO Employees (employee_id, first_name, last_name, role_id, hire_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, EmployeeFeild.getText());
            stmt.setString(2, EmployeeFirstNameFeild.getText());
            stmt.setString(3, EmployeeLastNameFeild.getText());
            stmt.setString(4, roleId); // Role ID
            stmt.setString(5, EmployeeDateHiredFeild.getText());
            stmt.executeUpdate();
            loadEmployeeTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add employee: " + e.getMessage());
        }
    }

    @FXML
    void updateEmployee(ActionEvent event) {
        String roleId = EmployeeRoleFeild.getText();

        // Validate Role ID
        if (!isValidRole(roleId)) {
            showAlert("Error", "Invalid Role ID!");
            return;
        }



        String query = "UPDATE Employees SET first_name = ?, last_name = ?, role_id = ?, hire_date = ? WHERE employee_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, EmployeeFirstNameFeild.getText());
            stmt.setString(2, EmployeeLastNameFeild.getText());
            stmt.setString(3, roleId); // Role ID
            stmt.setString(4, EmployeeDateHiredFeild.getText());
            stmt.setString(5, EmployeeFeild.getText());
            stmt.executeUpdate();
            loadEmployeeTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update employee: " + e.getMessage());
        }
    }

    private boolean isValidRole(String roleId) {
        String query = "SELECT 1 FROM Roles WHERE role_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to validate role: " + e.getMessage());
        }
        return false;
    }

    @FXML
    void deleteEmployee(ActionEvent event) {
        String query = "DELETE FROM Employees WHERE employee_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, EmployeeFeild.getText());
            stmt.executeUpdate();
            loadEmployeeTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete employee: " + e.getMessage());
        }
    }

    private void loadEmployeeTable() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        String query = "SELECT e.employee_id, e.first_name, e.last_name, r.role_name, r.salary, e.hire_date " +
                "FROM Employees e " +
                "JOIN Roles r ON e.role_id = r.role_id";

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employeeList.add(new Employee(
                        rs.getString("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("role_name"), // Role Name
                        rs.getDouble("salary"),   // Salary from Roles table
                        rs.getString("hire_date")
                ));
            }
            EmployeeTable.setItems(employeeList);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load employees: " + e.getMessage());
        }
    }

    private String[] getRoleDetails(String roleId) {
        String query = "SELECT role_name, salary FROM Roles WHERE role_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString("role_name"), String.valueOf(rs.getDouble("salary"))};
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to fetch role details: " + e.getMessage());
        }
        return null;
    }

    private String getRoleIdFromRoleName(String roleName) {
        String query = "SELECT role_id FROM Roles WHERE role_name = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role_id");
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to fetch role ID: " + e.getMessage());
        }
        return null;
    }

    @FXML
    void clearFields() {
        EmployeeFeild.clear();
        EmployeeFirstNameFeild.clear();
        EmployeeLastNameFeild.clear();
        EmployeeRoleFeild.clear();
        EmployeeDateHiredFeild.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane, "AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane, "Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane, "Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane, "Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane, "Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane, "Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane, "Visitors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane,"HomePage.fxml");
    }

    @FXML
    void goToRoles(ActionEvent event) throws IOException {
        new SceneSwitch(EmployeesPane,"RolesPage.fxml");
    }
}
