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

public class CareTypeController {

    @FXML
    private TextField Care_Type_IDFeild, Care_NameFeild;
    @FXML
    private TableView<CareType> AnimalCareTable;
    @FXML
    private TableColumn<CareType, Integer> Care_Type_Cell;
    @FXML
    private TableColumn<CareType, String> Care_Type_Name_Cell;
    @FXML
    private Button AddBtn, UpdateBtn, DeleteBtn, ClearBtn;
    @FXML
    private AnchorPane CareTypePane;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();

        // Configure table columns
        Care_Type_Cell.setCellValueFactory(new PropertyValueFactory<>("careTypeId"));
        Care_Type_Name_Cell.setCellValueFactory(new PropertyValueFactory<>("careTypeName"));

        loadCareTypeTable();

        // Double-click to populate fields
        AnimalCareTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                CareType selected = AnimalCareTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    Care_Type_IDFeild.setText(String.valueOf(selected.getCareTypeId()));
                    Care_NameFeild.setText(selected.getCareTypeName());
                }
            }
        });
    }

    @FXML
    void addCareType(ActionEvent event) {
        String insertQuery = "INSERT INTO CareType (care_type_name) VALUES (?)";
        try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
            stmt.setString(1, Care_NameFeild.getText());
            stmt.executeUpdate();
            loadCareTypeTable();
            clearFields();
            showAlert("Success", "Care type added successfully!");
        } catch (SQLException e) {
            showAlert("Error", "Failed to add care type: " + e.getMessage());
        }
    }

    @FXML
    void updateCareType(ActionEvent event) {
        String updateQuery = "UPDATE CareType SET care_type_name = ? WHERE care_type_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            stmt.setString(1, Care_NameFeild.getText());
            stmt.setInt(2, Integer.parseInt(Care_Type_IDFeild.getText()));
            stmt.executeUpdate();
            loadCareTypeTable();
            clearFields();
            showAlert("Success", "Care type updated successfully!");
        } catch (SQLException e) {
            showAlert("Error", "Failed to update care type: " + e.getMessage());
        }
    }

    @FXML
    void deleteCareType(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Care Type ID");
        dialog.setHeaderText("Enter the Care Type ID to delete:");
        dialog.setContentText("Care Type ID:");
        dialog.showAndWait().ifPresent(id -> {
            String deleteQuery = "DELETE FROM CareType WHERE care_type_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(deleteQuery)) {
                stmt.setInt(1, Integer.parseInt(id));
                stmt.executeUpdate();
                loadCareTypeTable();
                clearFields();
                showAlert("Success", "Care type deleted successfully!");
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete care type: " + e.getMessage());
            }
        });
    }

    @FXML
    void clearFields() {
        Care_Type_IDFeild.clear();
        Care_NameFeild.clear();
    }

    private void loadCareTypeTable() {
        ObservableList<CareType> careTypeList = FXCollections.observableArrayList();
        String query = "SELECT * FROM CareType";
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                careTypeList.add(new CareType(rs.getInt("care_type_id"), rs.getString("care_type_name")));
            }
            AnimalCareTable.setItems(careTypeList);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load care type data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(CareTypePane, "Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(CareTypePane, "Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(CareTypePane, "Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(CareTypePane, "Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(CareTypePane, "Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(CareTypePane, "Visitors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(CareTypePane, "HomePage.fxml");
    }

    @FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(CareTypePane,"AnimalCare.fxml");
    }
}