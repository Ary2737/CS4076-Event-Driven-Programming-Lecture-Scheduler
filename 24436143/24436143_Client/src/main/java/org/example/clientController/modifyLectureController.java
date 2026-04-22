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

    /*
      Method which handles "ADD"/"REMOVE" lecture buttons
     */

    @FXML
    private void handleLectureModification(ActionEvent event) {
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

        // Determining whether "ADD" or 'REMOVE" button was clicked
        // This will determine which action the user wants to do (ADD/REMOVE lecture)
        String modifyAction = " ";

        // getSource() = Tells us which button was clicked

        if(event.getSource() == addLectureButton){
            modifyAction = "ADD|";
        } else if(event.getSource() == removeLectureButton){
            modifyAction = "REMOVE|";
        }

        // Formatting client request string for server
        String clientRequest = modifyAction +  dayOfWeek + "|" +  timeSlot + "|" +  moduleCode + "|" + roomCode;

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


    /*
    Method which handles the "STOP CONNECTION" button
     */

    @FXML
    private void handleStopConnection(ActionEvent event) {

        // String to be sent to server (Client's request to shut down server)
        String clientRequest = "QUIT";

        // Informing server with "QUIT" client request
        try {
            // Client "QUIT" request is sent to server
            serverClientLog.appendText("CLIENT: Attempting to close server connection...\n");
            serverClientLog.appendText("CLIENT: Sending " + clientRequest + " to SERVER\n");
            out.println(clientRequest);

            // Server confirms that the connection is closed
            String serverResponse = in.readLine();
            serverClientLog.appendText("SERVER: " + serverResponse);


            // Closing the input/output streams
            if(out != null) out.close();
            if(in != null) in.close();

            // Closing the client's socket
            if(socket != null && !socket.isClosed()) socket.close();


            serverClientLog.appendText("SERVER: Connection successfully closed\n");

            // Disabling GUI buttons so that user can't submit add/remove requests
            addLectureButton.setDisable(true);
            removeLectureButton.setDisable(true);

        } catch (IOException e) {
            serverClientLog.appendText("SYSTEM: Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

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
