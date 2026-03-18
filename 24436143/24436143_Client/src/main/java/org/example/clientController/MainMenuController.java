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

    private @FXML Button display_lectures_button;
    @FXML private Button add_button;
    @FXML private Button remove_button;

    @FXML
    private void handleModifyButtons(ActionEvent e) {


        // Checking if the right buttons was clicked ("ADD Lecture" or "REMOVE Lecture" button)

        if (e.getSource() == add_button || e.getSource() == remove_button) {
            try {
                // Trying to load FXML file of modify lecture screen
                Parent loader = FXMLLoader.load(getClass().getResource("/modifyLecture.fxml"));

                // Creating scene with loaded FXML file
                Scene modifyLectureScene = new Scene(loader);

                // Getting current stage where the buttons were clicked
                Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

                // Swapping scenes around + displaying
                currentStage.setScene(modifyLectureScene);
                currentStage.show();


            } catch (IOException ex) {
                System.err.println("Unable to load next scene !");
                throw new RuntimeException("Unable to load next scene !");
            }

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
