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

public class RolesController {

    @FXML
    private TableView<Role> EmployeeTable;

    @FXML
    private TableColumn<Role, Integer> Role_IDCell;

    @FXML
    private TableColumn<Role, String> Role_NameCell;

    @FXML
    private TableColumn<Role, Double> Salary_Cell;

    @FXML
    private TextField Role_IDFeild, Role_NameFeild, Salary_Feild;

    @FXML
    private Button AddBtn, UpdateBtn, DeleteBtn, ClearBtn;

    @FXML
    private AnchorPane RolesPane;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();

        // Configure table columns
        Role_IDCell.setCellValueFactory(new PropertyValueFactory<>("roleId"));
        Role_NameCell.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        Salary_Cell.setCellValueFactory(new PropertyValueFactory<>("salary"));

        loadRolesTable();

        // Double-click to populate fields
        EmployeeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Role selectedRole = EmployeeTable.getSelectionModel().getSelectedItem();
                if (selectedRole != null) {
                    Role_IDFeild.setText(String.valueOf(selectedRole.getRoleId()));
                    Role_NameFeild.setText(selectedRole.getRoleName());
                    Salary_Feild.setText(String.valueOf(selectedRole.getSalary()));
                }
            }
        });
    }

    @FXML
    void addRole(ActionEvent event) {
        String insertQuery = "INSERT INTO Roles (role_name, salary) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
            stmt.setString(1, Role_NameFeild.getText());
            stmt.setDouble(2, Double.parseDouble(Salary_Feild.getText()));
            stmt.executeUpdate();
            loadRolesTable();
            clearFields();
            showAlert("Success", "Role added successfully!");
        } catch (SQLException e) {
            showAlert("Error", "Failed to add role: " + e.getMessage());
        }
    }

    @FXML
    void updateRole(ActionEvent event) {
        String updateQuery = "UPDATE Roles SET role_name = ?, salary = ? WHERE role_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            stmt.setString(1, Role_NameFeild.getText());
            stmt.setDouble(2, Double.parseDouble(Salary_Feild.getText()));
            stmt.setInt(3, Integer.parseInt(Role_IDFeild.getText()));
            stmt.executeUpdate();
            loadRolesTable();
            clearFields();
            showAlert("Success", "Role updated successfully!");
        } catch (SQLException e) {
            showAlert("Error", "Failed to update role: " + e.getMessage());
        }
    }

    @FXML
    void deleteRole(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Role ID");
        dialog.setHeaderText("Enter the Role ID to delete:");
        dialog.setContentText("Role ID:");
        dialog.showAndWait().ifPresent(id -> {
            String deleteQuery = "DELETE FROM Roles WHERE role_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(deleteQuery)) {
                stmt.setInt(1, Integer.parseInt(id));
                stmt.executeUpdate();
                loadRolesTable();
                clearFields();
                showAlert("Success", "Role deleted successfully!");
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete role: " + e.getMessage());
            }
        });
    }

    @FXML
    void clearFields() {
        Role_IDFeild.clear();
        Role_NameFeild.clear();
        Salary_Feild.clear();
    }

    private void loadRolesTable() {
        ObservableList<Role> rolesList = FXCollections.observableArrayList();
        String query = "SELECT * FROM Roles";
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rolesList.add(new Role(rs.getInt("role_id"), rs.getString("role_name"), rs.getDouble("salary")));
            }
            EmployeeTable.setItems(rolesList);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load roles data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(RolesPane, "AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(RolesPane, "Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(RolesPane, "Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(RolesPane, "Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(RolesPane, "Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(RolesPane, "Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(RolesPane, "Visitors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(RolesPane, "HomePage.fxml");
    }
}
