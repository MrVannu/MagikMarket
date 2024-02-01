package org.project.exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import java.util.Optional;

public class AmountNotAllowedException extends RuntimeException {
    public AmountNotAllowedException() {
        showAlert();
    }

    private void showAlert() {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("AMOUNT EXCEEDED!");
            alert.setHeaderText(null);
            alert.setContentText("You can add up to 10000 euro to your account!");

            ButtonType okButton = new ButtonType("Got it!", ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(okButton);
            Optional<ButtonType> result = alert.showAndWait();
    }

}