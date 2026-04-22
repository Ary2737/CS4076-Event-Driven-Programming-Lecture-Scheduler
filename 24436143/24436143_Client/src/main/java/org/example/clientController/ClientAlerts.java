package org.example.clientController;

import javafx.scene.control.Alert;
import javafx.application.Platform;


public class ClientAlerts {

    /**
     * Helper method for successful actions or standard information.
     */
    protected void showInformationAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null); // No header looks cleaner for info boxes
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Helper method to safely display an error popup on the UI thread.
     */
    public void showErrorAlert(String title, String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("Error");
            alert.setContentText(errorMessage);

            // showAndWait() pauses the UI until the user clicks "OK"
            alert.showAndWait();
        });
    }

}




