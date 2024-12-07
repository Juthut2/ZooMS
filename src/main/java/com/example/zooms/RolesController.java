package com.example.zooms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RolesController {
    @FXML
    private AnchorPane RolesPane;

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
        new SceneSwitch(RolesPane,"HomePage.fxml");
    }
}
