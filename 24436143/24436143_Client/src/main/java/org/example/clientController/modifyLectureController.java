/**
 * This class controls the GUI elements for the ADD lecture screen
 * This class handles user entering in lecture details, pressing buttons etc.
 */

package org.example.clientController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class modifyLectureController {
    // Declaring UI components of ADD/REMOVE screen

    // ChoiceBoxes
    @FXML private ChoiceBox<String> timeSlotChoiceBox;
    @FXML private ChoiceBox<String> dayChoiceBox;

    // TextFields + TextArea
    @FXML private TextField textFieldRoomCode;
    @FXML private TextField textFieldModuleCode;
    @FXML private TextArea serverClientLog;

    // Buttons
    @FXML private Button addLectureButton;
    @FXML private Button stopConnectionButton;
    @FXML public Button removeLectureButton;
    @FXML private Button returnMainMenuButton;


    // Networking variables
    private BufferedReader in;
    private PrintWriter out;


    // Initialises ChoiceBoxes automatically on run-time
    // Sets up TCP connection + input/output streams with the server application
    @FXML
    private void initialize() {
        dayChoiceBox.getItems().addAll("Monday", "Tuesday","Wednesday","Thursday","Friday");
        timeSlotChoiceBox.getItems().addAll(
                "09:00-10:00",
                "10:00-11:00",
                "11:00-12:00",
                "13:00-14:00",
                "14:00-15:00",
                "15:00-16:00",
                "16:00-17:00",
                "17:00-18:00"
        );
    }

    /*
      Method which handles "ADD"/"REMOVE" lecture buttons
     */

    @FXML
    private void handleLectureModification(ActionEvent event) {
        try {
            // Get data from user input/input UI
            String dayOfWeek = dayChoiceBox.getValue();
            String timeSlot = timeSlotChoiceBox.getValue();
            String roomCode = textFieldRoomCode.getText();
            String moduleCode = textFieldModuleCode.getText();

            // Ensure nothing is empty
            if (dayOfWeek == null || timeSlot == null || roomCode.isEmpty() || moduleCode.isEmpty()) {
                serverClientLog.appendText("SYSTEM: Please fill in all fields before adding.\n");
                // Prevents server from receiving empty request
                return;
            }

            // Determining whether "ADD" or "REMOVE" button was clicked
            // This will determine which action the user wants to do (ADD/REMOVE lecture)
            String modifyAction = " ";

            // getSource() = Tells us which button was clicked

            if(event.getSource() == addLectureButton){
                modifyAction = "ADD|";
            } else if(event.getSource() == removeLectureButton){
                modifyAction = "REMOVE|";
            }

            // Formatting client request string for server
            String clientRequest = modifyAction +  dayOfWeek + "|" +  timeSlot+ "|" +  moduleCode.trim() + "|"
                    + roomCode.trim();


            // Grabbing the shared input/output streams
            in = clientNetwork.getIn();
            out = clientNetwork.getOut();

            serverClientLog.appendText("CLIENT: Sending " + clientRequest + " to SERVER\n");
            out.println(clientRequest);

            String serverResponse = in.readLine();
            serverClientLog.appendText("SERVER: " + serverResponse);

        // If there is an error with the streams/server connection/processing
        } catch (IOException ex) {
            serverClientLog.appendText("SYSTEM: Error: " + ex.getMessage());
        }
    }


    /*
    Method which handles the "STOP CONNECTION" button
     */

    @FXML
    private void handleStopConnection(ActionEvent event) {
        // String to be sent to server (Client's request to shut down server)
        String clientRequest = "QUIT";

        // Client "QUIT" request is sent to server
        serverClientLog.appendText("CLIENT: Attempting to close server connection...\n");
        serverClientLog.appendText("CLIENT: Sending " + clientRequest + " to SERVER\n");
        out.println(clientRequest);

        clientNetwork.disconnect();
        serverClientLog.appendText("SYSTEM: Disconnected from server.\n");
        addLectureButton.setDisable(true);
        removeLectureButton.setDisable(true);


    }

    @FXML
    private void handleReturnMenu(ActionEvent e) {
        try {
            // Loading the main menu scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
            Parent root = loader.load();

            // Getting the current window (Stage) from the button that was clicked
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            // Create a new scene with the Main Menu and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println("Error loading Main Menu screen:");
            ex.printStackTrace();
        }

    }

}
