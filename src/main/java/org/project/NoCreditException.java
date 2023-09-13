package org.project;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NoCreditException extends RuntimeException {
    public NoCreditException() {
        showAlert();
    }

    private void showAlert() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("CREDIT MISSING!");
        alert.setHeaderText(null);
        alert.setContentText("You do not have enough credit, please consider to recharge your account!");
        alert.showAndWait();
    }
}
