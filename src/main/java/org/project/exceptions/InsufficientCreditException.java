package org.project.exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class InsufficientCreditException extends RuntimeException {
    private boolean alertShown = false;

    public InsufficientCreditException() {
        showAlert();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("You do not own this stock!");
        alert.setHeaderText(null);
        alert.setContentText("Currently, you do not own any pieces of the stock you are trying to sell.");

        ButtonType okButton = new ButtonType("Go back", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait();

        // Set the flag to true after showing the alert
        alertShown = true;
    }

    public boolean isAlertShown() {
        return alertShown;
    }
}
