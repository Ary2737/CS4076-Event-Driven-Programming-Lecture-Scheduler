package org.example.clientController;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.example.clientModel.Lecture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DisplayController {

    // UI Components
    @FXML private GridPane scheduleGrid;
    @FXML private Button displayButton;

    // Network/Stream variable
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final int SERVER_PORT = 2378;
    private final String SERVER_IP = "127.0.0.1";

    @FXML
    public void initialize() {
        // Attempt to set up connection with server application
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.err.println("Could not connect to server from Display screen !");
            displayButton.setDisable(true); // Disable button if the server connection doesn't work
        }
    }


    private List<Lecture> parseLectureData(String response) {
        // Create an empty list to hold the Lecture objects
        List<Lecture> list = new ArrayList<>();

        // Remove the "LECTURE_DATA|" header tag
        String rawData = response.replace("LECTURE_DATA|", "");

        // Split the string into individual rows using the colon ':'
        String[] rows = rawData.split(":");

        // Loop through each row
        for (String row : rows) {

            // Make sure the row isn't empty
            if (!row.isEmpty()) {

                // Split the row into its 4 specific fields using the comma ','
                String[] fields = row.split(",");

                // Rebuild the Lecture object
                Lecture rebuiltLecture = new Lecture(fields[0], fields[1], fields[2], fields[3]);

                // Add it to our list
                list.add(rebuiltLecture);
            }
        }

        // Return the fully populated list
        return list;
    }

    // Method to handle clicking of "Display Lectures" button

    @FXML
    public void handleDisplayButtonClick() {
        try {
            // Asking server for display
            out.println("DISPLAY|ALL");


            // Wait for the server to send the data back
            String serverResponse = in.readLine();

            if (serverResponse != null) {
                if (serverResponse.startsWith("LECTURE_DATA|")) {
                    // Decode the data and update the grid
                    List<Lecture> parsedLectures = parseLectureData(serverResponse);
                    populateGridWithLectures(parsedLectures);

                } else if (serverResponse.startsWith("SCHEDULE_EMPTY|")) {
                    // If empty, clear out the grid!
                    scheduleGrid.getChildren().removeIf(node -> node instanceof StackPane);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to fetch data from the server.");
        }
    }


    private void populateGridWithLectures(List<Lecture> receivedLectures) {

        // Clear any old blocks from the grid (keeps the header labels safe)
        scheduleGrid.getChildren().removeIf(node -> node instanceof StackPane);

        // Loop through the list and place each block
        for (Lecture l : receivedLectures) {

            int col = 0;
            switch (l.getDayOfWeek()) {
                case "Monday": col = 1; break;
                case "Tuesday": col = 2; break;
                case "Wednesday": col = 3; break;
                case "Thursday": col = 4; break;
                case "Friday": col = 5; break;
            }

            int row = 0;
            switch (l.getTimeSlot()) {
                case "09:00-10:00": row = 1; break;
                case "10:00-11:00": row = 2; break;
                case "11:00-12:00": row = 3; break;
                case "12:00-13:00": row = 4; break;
                case "13:00-14:00": row = 5; break;
                case "14:00-15:00": row = 6; break;
                case "15:00-16:00": row = 7; break;
                case "16:00-17:00": row = 8; break;
                case "17:00-18:00": row = 9; break;
            }

            // If the row and col are valid, draw it!
            if (col != 0 && row != 0) {
                StackPane visualBlock = createLectureBlock(l);

                scheduleGrid.add(visualBlock, col, row);

                // Make the block stretch to fill its cell completely
                GridPane.setFillWidth(visualBlock, true);
                GridPane.setFillHeight(visualBlock, true);
            }
        }
    }

    private StackPane createLectureBlock(Lecture lecture) {
        StackPane block = new StackPane();

        // Add a nice green background with rounded corners and margins
        block.setStyle("-fx-background-color: #4fff4f; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #246324;");

        // Add the module and room text inside the block
        Label infoLabel = new Label(lecture.getModuleCode() + "\n" + lecture.getRoomCode());
        infoLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-alignment: center;");

        block.getChildren().add(infoLabel);

        return block;
    }
}







