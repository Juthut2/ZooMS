package com.example.zooms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class RoleController {

    @FXML
    private Button AddBtn;

    @FXML
    private Button ClearBtn;

    @FXML
    private Button DeleteBtn;

    @FXML
    private Button UpdateBtn;

    @FXML
    private TextField RoleIDFeild;

    @FXML
    private TextField RoleNameFeild;

    @FXML
    private TextField RoleDescriptionFeild;

    @FXML
    private AnchorPane RolesPane;

    @FXML
    private TableView<?> RolesTable;

    private Connection con;
    // Navigate to different pages
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

    // Methods for handling CRUD operations on roles

    @FXML
    void addRole(ActionEvent event) {
        // Add code to insert the role into the database
        String roleId = RoleIDFeild.getText();
        String roleName = RoleNameFeild.getText();
        String roleDescription = RoleDescriptionFeild.getText();

        // Use DB connection here to insert into database
        // e.g. INSERT INTO Roles (role_id, role_name, role_description) VALUES (?, ?, ?)
    }

    @FXML
    void updateRole(ActionEvent event) {
        // Add code to update the role in the database
        String roleId = RoleIDFeild.getText();
        String roleName = RoleNameFeild.getText();
        String roleDescription = RoleDescriptionFeild.getText();

        // Use DB connection here to update the role details in the database
        // e.g. UPDATE Roles SET role_name = ?, role_description = ? WHERE role_id = ?
    }

    @FXML
    void deleteRole(ActionEvent event) {
        // Add code to delete the role from the database
        String roleId = RoleIDFeild.getText();

        // Use DB connection here to delete the role from the database
        // e.g. DELETE FROM Roles WHERE role_id = ?
    }

    @FXML
    void clearFields(ActionEvent event) {
        // Clear the input fields
        RoleIDFeild.clear();
        RoleNameFeild.clear();
        RoleDescriptionFeild.clear();
    }

    // Load roles into the table (you may need to adjust this part for your DB query)
    private void loadRolesTable() {
        // Create the SQL query to retrieve roles
        String query = "SELECT * FROM Roles";

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            // Process result set and populate the table view
            while (rs.next()) {
                // Add rows to the RolesTable (e.g. creating Role objects and adding to an ObservableList)
                // Example: roles.add(new Role(rs.getString("role_id"), rs.getString("role_name"), rs.getString("role_description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
