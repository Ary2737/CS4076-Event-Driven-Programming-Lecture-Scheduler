/**
 * This class controls the Main Menu screen
 * Allows user to press buttons to go to the ADD screen , DISPLAY screen etc.
 */

package org.example.clientController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {

    @FXML
    private void handleAddButton(ActionEvent e) {
        try {
            // Trying to load FXML file of add lecture screen
            Parent loader = FXMLLoader.load(getClass().getResource("/addLecture.fxml"));

            // Creating scene with loaded FXML file
            Scene addLectureScene = new Scene(loader);

            // Getting current stage where the add button was clicked
            Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            // Swapping scenes around + displaying
            currentStage.setScene(addLectureScene);
            currentStage.show();



        } catch (IOException ex) {
            System.err.println("Unable to load next scene !");
            throw new RuntimeException("Unable to load next scene !");
        }

    }

    // Method which handles the display button click
    // Loads display lecture screen

    @FXML
    public void handleDisplayButtonClick(ActionEvent e) {
        try {
            // Loading display FXML
            Parent loader = FXMLLoader.load(getClass().getResource("/displayLecture.fxml"));
            Scene displayScene = new Scene(loader);


            // Getting current stage where the display button was clicked
            Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            // Swapping scenes around + displaying
            currentStage.setScene(displayScene);
            currentStage.show();

        } catch (IOException ex) {
            // If you can't load scene
            System.err.println("Unable to load display scene !");
            ex.printStackTrace(); // Useful for debugging
            throw new RuntimeException("Unable to load display scene !");
        }


    }


}
