package com.example.zooms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnclosuresController {

    @FXML
    private TextField EnclosureIDFeild, EnclosureNameFeild, EnclosureTypeFeild, EnclosureCapacityFeild;
    @FXML
    private TableView<Enclosure> Enclosure_Table;
    @FXML
    private AnchorPane EnclosuresPane;
    @FXML
    private Button EnclosureAddBtn, EnclosureUpdateBtn, EnclosureDeleteBtn, EnclosureClearBtn;

    @FXML
    private TableColumn<?, ?> Enclosure_ID;
    @FXML
    private TableColumn<?, ?> Enclosure_Name;
    @FXML
    private TableColumn<?, ?> Enclosure_Capacity;
    @FXML
    private TableColumn<?, ?> Type;

    private Connection con;

    public void initialize() {
        con = DBconnectionZoo.ConnectionDB();

        // Initialize the table columns
        Enclosure_ID.setCellValueFactory(new PropertyValueFactory<>("enclosureId"));
        Enclosure_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Enclosure_Capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));

        loadEnclosuresTable();

        // Add double-click event listener to the table
        Enclosure_Table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Enclosure selectedEnclosure = Enclosure_Table.getSelectionModel().getSelectedItem();
                if (selectedEnclosure != null) {
                    // Populate text fields with the selected enclosure data
                    EnclosureIDFeild.setText(selectedEnclosure.getEnclosureId());
                    EnclosureNameFeild.setText(selectedEnclosure.getName());
                    EnclosureCapacityFeild.setText(String.valueOf(selectedEnclosure.getCapacity()));
                    EnclosureTypeFeild.setText(selectedEnclosure.getType());
                }
            }
        });
    }

    @FXML
    void addEnclosure(ActionEvent event) {
        String query = "INSERT INTO Enclosures (enclosure_name, enclosure_capacity, enclosure_type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, EnclosureNameFeild.getText());
            stmt.setInt(2, Integer.parseInt(EnclosureCapacityFeild.getText()));
            stmt.setString(3, EnclosureTypeFeild.getText());
            stmt.executeUpdate();
            loadEnclosuresTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add enclosure: " + e.getMessage());
        }
    }

    @FXML
    void updateEnclosure(ActionEvent event) {
        String updateQuery = "UPDATE Enclosures SET enclosure_name = ?, enclosure_type = ?, enclosure_capacity = ? WHERE enclosure_id = ?";

        // Ensure all fields are filled
        if (EnclosureIDFeild.getText().isEmpty() || EnclosureNameFeild.getText().isEmpty() ||
                EnclosureTypeFeild.getText().isEmpty() || EnclosureCapacityFeild.getText().isEmpty()) {
            showAlert("Error", "All fields must be filled out before updating.");
            return;
        }

        try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
            // Set values from text fields
            updateStmt.setString(1, EnclosureNameFeild.getText());
            updateStmt.setString(2, EnclosureTypeFeild.getText());
            updateStmt.setInt(3, Integer.parseInt(EnclosureCapacityFeild.getText()));
            updateStmt.setString(4, EnclosureIDFeild.getText()); // Use the ID from the text field for update

            // Execute the update query
            updateStmt.executeUpdate();

            // Reload the table to reflect the updated data
            loadEnclosuresTable();

            // Clear the text fields after the update
            clearFields();

            // Show success alert
            showAlert("Success", "Enclosure updated successfully!");
        } catch (SQLException updateException) {
            showAlert("Error", "Failed to update enclosure: " + updateException.getMessage());
        }
    }


    @FXML
    void deleteEnclosure(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Enclosure ID");
        dialog.setHeaderText("Enter the Enclosure ID to delete:");
        dialog.setContentText("Enclosure ID:");
        dialog.showAndWait().ifPresent(id -> {
            String query = "DELETE FROM Enclosures WHERE enclosure_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
                resetAutoIncrement();
                loadEnclosuresTable();
                clearFields();
                showAlert("Success", "Enclosure deleted successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete enclosure: " + e.getMessage());
            }
        });
    }

    private void resetAutoIncrement() {
        String resetQuery = "ALTER TABLE Enclosures AUTO_INCREMENT = 1";
        try (PreparedStatement stmt = con.prepareStatement(resetQuery)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Error", "Failed to reset auto-increment: " + e.getMessage());
        }
    }

    @FXML
    void clearFields() {
        EnclosureIDFeild.clear();
        EnclosureNameFeild.clear();
        EnclosureTypeFeild.clear();
        EnclosureCapacityFeild.clear();
    }

    private void loadEnclosuresTable() {
        ObservableList<Enclosure> enclosureList = FXCollections.observableArrayList();
        String query = "SELECT * FROM enclosures";
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String enclosureId = rs.getString("enclosure_id");
                // Skip the enclosure with a zero enclosure_id
                if ("0".equals(enclosureId)) {
                    continue;
                }

                enclosureList.add(new Enclosure(
                        enclosureId,
                        rs.getString("enclosure_name"),
                        rs.getInt("enclosure_capacity"),
                        rs.getString("enclosure_type")
                ));
            }
            Enclosure_Table.setItems(enclosureList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void searchEnclosure(ActionEvent event) throws IOException {
        // Show a dialog to prompt the user for the Enclosure ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Enclosure");
        dialog.setHeaderText("Enter Enclosure ID");
        dialog.setContentText("Enclosure ID:");
        dialog.showAndWait().ifPresent(enclosureId -> {
            try {
                // Corrected FXML file name and controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AnimalsInEnclosure.fxml"));
                Parent root = loader.load();

                // Pass the Enclosure_ID to InsideEnclosureController
                InsideEnclosureController controller = loader.getController();
                controller.setEnclosureIdAndLoadData(enclosureId);

                // Switch the scene
                Stage stage = (Stage) EnclosuresPane.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                showAlert("Error", "Failed to load Inside Enclosure view: " + e.getMessage());
            }
        });
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(EnclosuresPane, "AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) throws IOException {
        new SceneSwitch(EnclosuresPane, "Animals.fxml");
    }

    @FXML
    void goToEmployees(ActionEvent event) throws IOException {
        new SceneSwitch(EnclosuresPane, "Employees.fxml");
    }

    @FXML
    void goToEnclosures(ActionEvent event) throws IOException {
        new SceneSwitch(EnclosuresPane, "Enclosures.fxml");
    }

    @FXML
    void goToEvents(ActionEvent event) throws IOException {
        new SceneSwitch(EnclosuresPane, "Events.fxml");
    }

    @FXML
    void goToTickets(ActionEvent event) throws IOException {
        new SceneSwitch(EnclosuresPane, "Tickets.fxml");
    }

    @FXML
    void goToVisitors(ActionEvent event) throws IOException {
        new SceneSwitch(EnclosuresPane, "Vistors.fxml");
    }

    @FXML
    void goToHomePage(ActionEvent event) throws IOException {
        new SceneSwitch(EnclosuresPane,"HomePage.fxml");


    }
}
