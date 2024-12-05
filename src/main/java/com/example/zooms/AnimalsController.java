package com.example.zooms;

import com.example.zooms.Animal;
import com.example.zooms.DBconnectionZoo;
import com.example.zooms.SceneSwitch;
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

public class AnimalsController {
    @FXML
    private TextField AnimalNameFeild;
    @FXML
    private Button AnimalsAddBtn;
    @FXML
    private TextField AnimalsAgeFeild;
    @FXML
    private Button AnimalsClearBtn;
    @FXML
    private TextField AnimalsDOBFeild;
    @FXML
    private Button AnimalsDeleteBtn;
    @FXML
    private TextField AnimalsEnclosureIDfeild;
    @FXML
    private TextField AnimalsIDFeild;
    @FXML
    private AnchorPane AnimalsPane;
    @FXML
    private TextField AnimalsSpeciesFeild;
    @FXML
    private TableView<Animal> AnimalsTable;
    @FXML
    private Button AnimalsUpdateBtn;
    @FXML
    private ComboBox<String> Animals_SexFeild;
    @FXML
    private TextField Animals_StatusFeild;

    @FXML
    private TableColumn<Animal, String> AnimalIDColumn;
    @FXML
    private TableColumn<Animal, String> AnimalNameColumn;
    @FXML
    private TableColumn<Animal, String> AnimalSpeciesColumn;
    @FXML
    private TableColumn<Animal, String> AnimalSexColumn;
    @FXML
    private TableColumn<Animal, Integer> AnimalAgeColumn;
    @FXML
    private TableColumn<Animal, String> AnimalStatusColumn;
    @FXML
    private TableColumn<Animal, String> AnimalDOBColumn;
    @FXML
    private TableColumn<Animal, String> AnimalEnclosureIDColumn;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();
        Animals_SexFeild.setItems(FXCollections.observableArrayList("Male", "Female"));
        initializeTable();  // Ensure this is here to initialize the table columns
        loadAnimalsTable();
    }

    @FXML
    void addAnimal(ActionEvent event) {
        String query = "INSERT INTO Animals (animal_id, enclosure_id, animal_name, animal_sex, animal_age, animal_species, animal_status, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, AnimalsIDFeild.getText());
            stmt.setString(2, AnimalsEnclosureIDfeild.getText());
            stmt.setString(3, AnimalNameFeild.getText());
            stmt.setString(4, Animals_SexFeild.getValue());
            stmt.setInt(5, Integer.parseInt(AnimalsAgeFeild.getText()));
            stmt.setString(6, AnimalsSpeciesFeild.getText());
            stmt.setString(7, Animals_StatusFeild.getText());
            stmt.setString(8, AnimalsDOBFeild.getText());
            stmt.executeUpdate();
            loadAnimalsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add animal: " + e.getMessage());
        }
    }

    @FXML
    void deleteAnimal(ActionEvent event) {
        String query = "DELETE FROM Animals WHERE animal_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, AnimalsIDFeild.getText());
            stmt.executeUpdate();
            loadAnimalsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete animal: " + e.getMessage());
        }
    }

    @FXML
    void updateAnimal(ActionEvent event) {
        String query = "UPDATE Animals SET animal_name = ?, animal_species = ?, date_of_birth = ?, animal_age = ?, animal_sex = ?, animal_status = ?, enclosure_id = ? WHERE animal_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, AnimalNameFeild.getText());
            stmt.setString(2, AnimalsSpeciesFeild.getText());
            stmt.setString(3, AnimalsDOBFeild.getText());
            stmt.setInt(4, Integer.parseInt(AnimalsAgeFeild.getText()));
            stmt.setString(5, Animals_SexFeild.getValue());
            stmt.setString(6, Animals_StatusFeild.getText());
            stmt.setString(7, AnimalsEnclosureIDfeild.getText());
            stmt.setString(8, AnimalsIDFeild.getText());
            stmt.executeUpdate();
            loadAnimalsTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to update animal: " + e.getMessage());
        }
    }

    @FXML
    void clearFields() {
        AnimalsIDFeild.clear();
        AnimalNameFeild.clear();
        AnimalsSpeciesFeild.clear();
        AnimalsDOBFeild.clear();
        AnimalsAgeFeild.clear();
        Animals_SexFeild.setValue(null);
        Animals_StatusFeild.clear();
        AnimalsEnclosureIDfeild.clear();
    }

    private void loadAnimalsTable() {
        ObservableList<Animal> animals = FXCollections.observableArrayList();
        String query = "SELECT * FROM Animals"; // Ensure the query is correct
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                animals.add(new Animal(
                        rs.getString("animal_id"),
                        rs.getString("animal_name"),
                        rs.getString("animal_species"),
                        rs.getString("animal_sex"),
                        rs.getInt("animal_age"),
                        rs.getString("animal_status"),
                        rs.getString("date_of_birth"),
                        rs.getString("enclosure_id")
                ));
                System.out.println("Added animal: " + rs.getString("animal_name"));
            }

            AnimalsTable.setItems(animals); // Set the data to the table
        } catch (Exception e) {
            showAlert("Error", "Failed to load animals: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // CellValueFactories
    @FXML
    private void initializeTable() {
        AnimalIDColumn.setCellValueFactory(new PropertyValueFactory<>("animal_id"));
        AnimalNameColumn.setCellValueFactory(new PropertyValueFactory<>("animal_name"));
        AnimalSpeciesColumn.setCellValueFactory(new PropertyValueFactory<>("animal_species"));
        AnimalSexColumn.setCellValueFactory(new PropertyValueFactory<>("animal_sex"));
        AnimalAgeColumn.setCellValueFactory(new PropertyValueFactory<>("animal_age"));
        AnimalStatusColumn.setCellValueFactory(new PropertyValueFactory<>("animal_status"));
        AnimalDOBColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));
        AnimalEnclosureIDColumn.setCellValueFactory(new PropertyValueFactory<>("enclosure_id"));
    }

    @FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsPane,"AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) {}

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsPane,"Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsPane,"Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsPane,"Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsPane,"Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsPane,"Visitors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsPane,"HomePage.fxml");
    }
}
