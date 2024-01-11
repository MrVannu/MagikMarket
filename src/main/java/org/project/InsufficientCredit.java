package org.project;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class InsufficientCredit extends RuntimeException {
    public InsufficientCredit() {
        showAlert();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("You do not own this stock!");
        alert.setHeaderText(null);
        alert.setContentText("Currently, you do not own any pieces of the stok you are trying to sell.");

        ButtonType okButton = new ButtonType("Go back", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);
        Optional<ButtonType> result = alert.showAndWait();
    }

}