package com.example.zooms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AnimalsController {

    @FXML
    private AnchorPane AnimalsPane;

    @FXML
    void goToAnimalCare(ActionEvent event) throws IOException {
        new SceneSwitch(AnimalsPane,"AnimalCare.fxml");
    }

    @FXML
    void goToAnimals(ActionEvent event) {

    }

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

}
