package com.example.zooms;

import com.example.zooms.Animal;
import com.example.zooms.DBconnectionZoo;
import com.example.zooms.SceneSwitch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class InsideEnclosureController {

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

    @FXML
    private AnchorPane AnimalsInEnclosuresPane;

    @FXML
    private TableView<Animal> AnimalsTable; // Changed from TableView<?> to TableView<Animal>

    @FXML
    private Label EnclosureNameLabel;

    @FXML
    private Button GoBackButton;

    @FXML
    private TextField SearchField;

    private Connection con;
    private ObservableList<Animal> animalList = FXCollections.observableArrayList();

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();

        // Initialize table columns
        AnimalIDColumn.setCellValueFactory(new PropertyValueFactory<>("animal_id"));
        AnimalNameColumn.setCellValueFactory(new PropertyValueFactory<>("animal_name"));
        AnimalSpeciesColumn.setCellValueFactory(new PropertyValueFactory<>("animal_species"));
        AnimalSexColumn.setCellValueFactory(new PropertyValueFactory<>("animal_sex"));
        AnimalAgeColumn.setCellValueFactory(new PropertyValueFactory<>("animal_age"));
        AnimalStatusColumn.setCellValueFactory(new PropertyValueFactory<>("animal_status"));
        AnimalDOBColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));
        AnimalEnclosureIDColumn.setCellValueFactory(new PropertyValueFactory<>("enclosure_id"));
    }

    // Load data for a specific Enclosure ID
    // Load data for a specific Enclosure ID
    public void setEnclosureIdAndLoadData(String enclosureId) {
        // Query to get enclosure name
        String enclosureQuery = "SELECT enclosure_name FROM Enclosures WHERE enclosure_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(enclosureQuery)) {
            stmt.setString(1, enclosureId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String enclosureName = rs.getString("enclosure_name");

                // Query to get the total number of animals in this enclosure
                String countQuery = "SELECT COUNT(*) AS total FROM Animals WHERE enclosure_id = ?";
                try (PreparedStatement countStmt = con.prepareStatement(countQuery)) {
                    countStmt.setString(1, enclosureId);
                    ResultSet countRs = countStmt.executeQuery();
                    if (countRs.next()) {
                        int totalAnimals = countRs.getInt("total");
                        // Set the label with the enclosure name and the total number of animals
                        EnclosureNameLabel.setText(enclosureName + " (total number of animals: " + totalAnimals + ")");
                    }
                }
            } else {
                EnclosureNameLabel.setText("Enclosure not found!");
                return;
            }
        } catch (SQLException e) {
            EnclosureNameLabel.setText("Error fetching Enclosure name");
            e.printStackTrace();
            return;
        }

        // Query to load animals for the given Enclosure ID
        String animalQuery = "SELECT * FROM Animals WHERE enclosure_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(animalQuery)) {
            stmt.setString(1, enclosureId);
            ResultSet rs = stmt.executeQuery();
            animalList.clear();
            while (rs.next()) {
                animalList.add(new Animal(
                        rs.getString("animal_id"),
                        rs.getString("animal_name"),
                        rs.getString("animal_species"),
                        rs.getString("animal_sex"),
                        rs.getInt("animal_age"),
                        rs.getString("animal_status"),
                        rs.getString("date_of_birth"),
                        rs.getString("enclosure_id")
                ));
            }
            AnimalsTable.setItems(animalList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void searchAnimals(ActionEvent event) {
        String enclosureId = SearchField.getText();
        if (!enclosureId.isEmpty()) {
            setEnclosureIdAndLoadData(enclosureId);
        } else {
            EnclosureNameLabel.setText("Please enter a valid Enclosure ID.");
        }
    }

    @FXML
    void goBackToEnclosures(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Enclosures.fxml"));
        Stage stage = (Stage) AnimalsTable.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsInEnclosuresPane, "AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsInEnclosuresPane, "Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsInEnclosuresPane, "Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsInEnclosuresPane, "Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsInEnclosuresPane, "Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsInEnclosuresPane, "Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsInEnclosuresPane, "Vistors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsInEnclosuresPane, "HomePage.fxml");
    }
}
