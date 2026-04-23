package org.example.clientController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.clientModel.Lecture;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DisplayController extends ClientAlerts {

    // UI Components
    @FXML private GridPane timetableGrid;
    @FXML private Button displayButton;
    @FXML private Button returnMenuButton;

    // Stream variables
    private BufferedReader in;
    private PrintWriter out;

    public void initialize(){
        // Setting up shared input/output streams
        in = clientNetwork.getIn();
        out = clientNetwork.getOut();
    }

    private List<Lecture> parseLectureData(String response) {
        // Create an empty list to hold the Lecture objects
        List<Lecture> list = new ArrayList<>();

        // We add the length of the tag so it skips completely past it!
        String cleanData = response.substring(response.indexOf("LECTURE_DATA|") + "LECTURE_DATA|".length());

        System.out.println("Cleaned data being parsed: '" + cleanData + "'");

        // Split the string into individual rows using the semicolon ';'
        // (can't use ':' since the timeSlot itself contains colons e.g. "09:00-10:00")
        String[] rows = cleanData.split(";");

        // Loop through each row
        for (String row : rows) {
            if (!row.isEmpty()) {
                // Split the row into its 4 specific fields using the comma ','
                String[] fields = row.split(",");

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
        System.out.println("Button was clicked!");

        // Background Task for fetching timetable data
        Task<String> fetchTimetableData = new Task<String>() {
            @Override
            protected String call() throws Exception {
                // Send request + wait on the Background Thread
                System.out.println("CLIENT: Requesting DISPLAY data...");
                out.println("DISPLAY");
                return in.readLine(); // The task returns this string when finished
            }
        };

        // Defining what happens when the timetable data is fetched
        fetchTimetableData.setOnSucceeded(e -> {
            // Grab the string that was returned by the call() method above
            String serverResponse = fetchTimetableData.getValue();
            System.out.println("SERVER: " + serverResponse);

            if (serverResponse != null && serverResponse.startsWith("LECTURE_DATA|")) {

                //  Parse the string into a List of Lecture objects
                List<Lecture> parsedLectures = parseLectureData(serverResponse);

                // Pass that list directly to your fantastic population method!
                populateGridWithLectures(parsedLectures);

            } else if (serverResponse != null && serverResponse.equals("SCHEDULE_EMPTY")) {
                timetableGrid.getChildren().removeIf(node -> node instanceof StackPane); // Clear grid just in case
                showInformationAlert("Timetable Empty", "There are no lectures scheduled yet.");
            } else {
                showErrorAlert("Data Error", "Received invalid data from server.");
            }
        });

        // Defining what happens if the Network fails/drops
        fetchTimetableData.setOnFailed(e -> {
            Throwable error = fetchTimetableData.getException();
            showErrorAlert("Connection Error", "Failed to fetch data: " + error.getMessage());
        });

        // Start the worker thread!
        new Thread(fetchTimetableData).start();
    }

    private void populateGridWithLectures(List<Lecture> receivedLectures) {
        // Clear any old blocks from the grid (keeps the header labels safe)
        timetableGrid.getChildren().removeIf(node -> node instanceof StackPane);

        // Loop through the list and place each block
        for (Lecture l : receivedLectures) {
            System.out.println("Attempting to draw block for Day: '" + l.getDayOfWeek()+ "' at Time: '" + l.getTimeSlot() + "'");

            int col = 0;
            switch (l.getDayOfWeek().trim()) {
                case "Monday": col = 1; break;
                case "Tuesday": col = 2; break;
                case "Wednesday": col = 3; break;
                case "Thursday": col = 4; break;
                case "Friday": col = 5; break;
            }

            int row = 0;
            switch (l.getTimeSlot().trim()) {
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
                timetableGrid.add(visualBlock, col, row);

                // Make the block stretch to fill its cell completely
                GridPane.setFillWidth(visualBlock, true);
                GridPane.setFillHeight(visualBlock, true);
            }
        }
    }

    private StackPane createLectureBlock(Lecture lecture) {
        StackPane block = new StackPane();

        // Darker green to match the header buttons, with rounded corners
        block.setStyle("-fx-background-color: #389421; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #246324;");

        // "LEC" tag pinned to the top of the block
        Label lecLabel = new Label("LEC");
        lecLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 10;");
        StackPane.setAlignment(lecLabel, Pos.TOP_CENTER);
        StackPane.setMargin(lecLabel, new Insets(4, 0, 0, 0));

        // Module + room centered in the block
        Label infoLabel = new Label(lecture.getModuleCode() + "\n" + lecture.getRoomCode());
        infoLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-alignment: center;");
        StackPane.setAlignment(infoLabel, Pos.CENTER);

        block.getChildren().addAll(lecLabel, infoLabel);

        return block;
    }

    @FXML
    private void handleMenuReturn(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            showErrorAlert("Error loading Main Menu", "Failed to load Main Menu screen.");
            System.err.println("Error loading Main Menu screen:");
            ex.printStackTrace();
        }
    }
}







