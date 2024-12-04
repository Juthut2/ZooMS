package com.example.zooms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Updated FXML path
        FXMLLoader fxmlLoader = new FXMLLoader(HomePage.class.getResource("HomePage.fxml"));


        // Load the FXML file and set the scene
        Scene scene = new Scene(fxmlLoader.load());

        // Set the title and display the stage
        stage.setTitle("Modeng Zoo Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
