package com.example.zooms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CareTypeController {
    @FXML
    private AnchorPane CareTypePane;

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