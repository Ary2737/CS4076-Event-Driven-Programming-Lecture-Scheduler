/**
 * This class controls the GUI elements for the ADD lecture screen
 * This class handles user entering in lecture details, pressing buttons etc.
 */

package org.example.clientController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AddLectureController {

    // Declaring UI components of ADD screen

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


    // input/output streams
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Declaring variables to set up networking variables


    private static final String SERVER_IP = "127.0.0.1"; // Default IP for localhost. Easier testing !
    private static final int SERVER_PORT = 2378;


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

        try {
            
            socket = new Socket(SERVER_IP,SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Default server opening message
            // Places this message in textArea for client-server logs
            serverClientLog.appendText("SERVER: Connected to Server at " + SERVER_IP + ":" + SERVER_PORT + "\n");
            // If there is an error with streams/connection
        } catch(IOException ex) {
            serverClientLog.appendText("SYSTEM: Error: Could not connect to server. Is it running?\n");
            addLectureButton.setDisable(true); // Disable the button if we have no connection
        }
    }

    // Method which handles "ADD Lecture" Button

    @FXML
    private void handleAddLectureClick() {
        // Get data from user input/input UI
        String dayOfWeek = dayChoiceBox.getValue();
        String timeSlot = timeSlotChoiceBox.getValue();
        String roomCode = textFieldRoomCode.getText();
        String moduleCode = textFieldModuleCode.getText();

        // Ensure nothing is empty
        if (dayOfWeek == null || timeSlot == null || roomCode.isEmpty() || moduleCode.isEmpty()) {
            serverClientLog.appendText("SYSTEM: Please fill in all fields before adding.\n");

        }

        // Formatting client request string for server
        String clientRequest = "ADD|" +  dayOfWeek + "|" +  timeSlot + "|" +  moduleCode + "|" + roomCode;

        // Attempt to send the client request to the server
        try {
            serverClientLog.appendText("CLIENT: Sending " + clientRequest + " to SERVER\n");
            out.println(clientRequest);

            String serverResponse = in.readLine();
            serverClientLog.appendText("SERVER: " + serverResponse);

            // If there is an error with the streams/server connection/processing
        } catch (IOException ex) {
            serverClientLog.appendText("SYSTEM: Error: " + ex.getMessage());
        }
    }

}
