package com.example.zooms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeesController {



        @FXML
        private TextField EmployeeFeild, EmployeeFirstNameFeild, EmployeeLastNameFeild, EmployeeRoleFeild, EmployeeSalaryFeild, EmployeeDateHiredFeild;
        @FXML
        private TableView<Employee> EmployeeTable;
        @FXML
        private AnchorPane EmployeesPane;
        @FXML
        private Button AddBtn, UpdateBtn, DeleteBtn, ClearBtn;

        private Connection con;

        public void initialize() {
            con = DBconnectionZoo.ConnectionDB();
            loadEmployeeTable();
        }

        @FXML
        void addEmployee(ActionEvent event) {
            String query = "INSERT INTO Employees (employee_id, first_name, last_name, role, salary, date_hired) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, EmployeeFeild.getText());
                stmt.setString(2, EmployeeFirstNameFeild.getText());
                stmt.setString(3, EmployeeLastNameFeild.getText());
                stmt.setString(4, EmployeeRoleFeild.getText());
                stmt.setDouble(5, Double.parseDouble(EmployeeSalaryFeild.getText()));
                stmt.setString(6, EmployeeDateHiredFeild.getText());
                stmt.executeUpdate();
                loadEmployeeTable();
                clearFields();
            } catch (Exception e) {
                showAlert("Error", "Failed to add employee: " + e.getMessage());
            }
        }

        @FXML
        void updateEmployee(ActionEvent event) {
            String query = "UPDATE Employees SET first_name = ?, last_name = ?, role = ?, salary = ?, date_hired = ? WHERE employee_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, EmployeeFirstNameFeild.getText());
                stmt.setString(2, EmployeeLastNameFeild.getText());
                stmt.setString(3, EmployeeRoleFeild.getText());
                stmt.setDouble(4, Double.parseDouble(EmployeeSalaryFeild.getText()));
                stmt.setString(5, EmployeeDateHiredFeild.getText());
                stmt.setString(6, EmployeeFeild.getText());
                stmt.executeUpdate();
                loadEmployeeTable();
                clearFields();
            } catch (Exception e) {
                showAlert("Error", "Failed to update employee: " + e.getMessage());
            }
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

        @FXML
        void clearFields() {
            EmployeeFeild.clear();
            EmployeeFirstNameFeild.clear();
            EmployeeLastNameFeild.clear();
            EmployeeRoleFeild.clear();
            EmployeeSalaryFeild.clear();
            EmployeeDateHiredFeild.clear();
        }

    private void loadEmployeeTable() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        String query = "SELECT * FROM Employees";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employeeList.add(new Employee(
                        rs.getString("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("role"),
                        rs.getDouble("salary"),  // `salary` is now being retrieved as double
                        rs.getString("date_hired")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
