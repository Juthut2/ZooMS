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
    }

    @FXML
    void addEnclosure(ActionEvent event) {
        String query = "INSERT INTO Enclosures (enclosure_name, enclosure_capacity, enclosure_type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            // Set values for name, capacity, and type (no need to set enclosure_id)
            stmt.setString(1, EnclosureNameFeild.getText());
            stmt.setInt(2, Integer.parseInt(EnclosureCapacityFeild.getText()));
            stmt.setString(3, EnclosureTypeFeild.getText());

            // Execute the insert
            stmt.executeUpdate();

            // Reload the table to show the updated list
            loadEnclosuresTable();

            // Clear the fields
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add enclosure: " + e.getMessage());
        }
    }


    @FXML
    void updateEnclosure(ActionEvent event) {
        // Prompt the user to enter the enclosure ID (this part is kept from the previous update)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Enclosure ID");
        dialog.setHeaderText("Enter the Enclosure ID to update:");
        dialog.setContentText("Enclosure ID:");

        // Wait for the user input
        dialog.showAndWait().ifPresent(id -> {
            try {
                // Fetch the enclosure data based on the ID
                String query = "SELECT * FROM Enclosures WHERE enclosure_id = ?";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setString(1, id);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        // Fill the text fields with the fetched data
                        EnclosureIDFeild.setText(rs.getString("enclosure_id"));
                        EnclosureNameFeild.setText(rs.getString("enclosure_name"));
                        EnclosureCapacityFeild.setText(String.valueOf(rs.getInt("enclosure_capacity")));
                        EnclosureTypeFeild.setText(rs.getString("enclosure_type"));

                        // Now, when the user edits and clicks Update again, this code will save the new data
                        EnclosureUpdateBtn.setOnAction(e -> {
                            // Perform update on the enclosure in the database
                            String updateQuery = "UPDATE Enclosures SET enclosure_name = ?, enclosure_type = ?, enclosure_capacity = ? WHERE enclosure_id = ?";
                            try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                                updateStmt.setString(1, EnclosureNameFeild.getText());
                                updateStmt.setString(2, EnclosureTypeFeild.getText());
                                updateStmt.setInt(3, Integer.parseInt(EnclosureCapacityFeild.getText()));
                                updateStmt.setString(4, EnclosureIDFeild.getText());
                                updateStmt.executeUpdate();

                                // Reload the table to show updated data
                                loadEnclosuresTable();
                                clearFields();  // Clear fields after the update
                                showAlert("Success", "Enclosure updated successfully!");
                            } catch (SQLException updateException) {
                                showAlert("Error", "Failed to update enclosure: " + updateException.getMessage());
                            }
                        });

                    } else {
                        showAlert("Error", "Enclosure not found for ID: " + id);
                    }
                } catch (SQLException e) {
                    showAlert("Error", "Failed to fetch enclosure data: " + e.getMessage());
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to update enclosure: " + e.getMessage());
            }
        });
    }


    @FXML
    void deleteEnclosure(ActionEvent event) {
        // Prompt the user to enter the Enclosure ID to delete
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Enclosure ID");
        dialog.setHeaderText("Enter the Enclosure ID to delete:");
        dialog.setContentText("Enclosure ID:");

        dialog.showAndWait().ifPresent(id -> {
            // Delete the enclosure record based on the provided ID
            String query = "DELETE FROM Enclosures WHERE enclosure_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, id);
                stmt.executeUpdate();

                // Reset the auto-increment (for MySQL as an example)
                resetAutoIncrement();

                // Reload the table to show the updated data
                loadEnclosuresTable();
                clearFields();
                showAlert("Success", "Enclosure deleted successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete enclosure: " + e.getMessage());
            }
        });
    }

    private void resetAutoIncrement() {
        // Adjust this for your database. Example for MySQL:
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
        String query = "SELECT * FROM enclosures";  // Select all columns from the enclosures table
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                enclosureList.add(new Enclosure(
                        rs.getString("enclosure_id"),      // Auto-incremented ID
                        rs.getString("enclosure_name"),    // Name of the enclosure
                        rs.getInt("enclosure_capacity"),   // Capacity of the enclosure
                        rs.getString("enclosure_type")     // Type of the enclosure
                ));
            }
            // Set the items in the table
            Enclosure_Table.setItems(enclosureList);
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
