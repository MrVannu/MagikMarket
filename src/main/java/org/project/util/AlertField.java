package org.project.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AlertField {
    // Method to show an error alert
    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show an information alert
    public static void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Set invalid field by changing css
    public static void invalidField(TextField... field){
        for(TextField f : field){
            f.setText("");
            f.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
        }
    }

    // Reset style of the field and empty the field
    public static void resetField(TextField... field){
        for(TextField f : field){
            f.setStyle("");
            f.setText("");
        }
    }
}