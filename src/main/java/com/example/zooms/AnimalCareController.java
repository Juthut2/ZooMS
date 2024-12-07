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

public class AnimalCareController {

    @FXML
    private TextField Animal_IDFeild, CareDateFeild, CareIDFeild, CareTypeFeild, EmployeeIDFeild;
    @FXML
    private TableView<AnimalCare> AnimalCareTable;
    @FXML
    private AnchorPane AnimalCarePane;
    @FXML
    private AnchorPane animalCare_form;
    @FXML
    private Button AddBtn, UpdateBtn, DeleteBtn, ClearBtn, Caretype;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();
        loadAnimalCareTable();
    }

    @FXML
    void addAnimalCare(ActionEvent event) {
        String query = "INSERT INTO AnimalCare (care_id, animal_id, employee_id, care_date, care_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
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
        String query = "UPDATE AnimalCare SET animal_id = ?, employee_id = ?, care_date = ?, care_type = ? WHERE care_id = ?";
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

    @FXML
    void deleteAnimalCare(ActionEvent event) {
        String query = "DELETE FROM AnimalCare WHERE care_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, CareIDFeild.getText());
            stmt.executeUpdate();
            loadAnimalCareTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete animal care: " + e.getMessage());
        }
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
        String query = "SELECT * FROM AnimalCare";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                animalCareList.add(new AnimalCare(
                        rs.getString("care_id"),
                        rs.getString("animal_id"),
                        rs.getString("employee_id"),
                        rs.getString("care_date"),
                        rs.getString("care_type")
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
    void goToAnimalCare(ActionEvent event) {

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
        new SceneSwitch(AnimalCarePane,"HomePage.fxml");
    }

    @FXML
    void goToCareType(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalCarePane, "CareType.fxml");
    }
}
