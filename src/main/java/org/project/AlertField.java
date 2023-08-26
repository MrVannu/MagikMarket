package org.project;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AlertField {

    // Defining method to show an alert
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Setting invalid field
    public static void invalidField(TextField... field){
        for(TextField f : field){
            f.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5px;");
            f.setText("");
        }
    }

    public static void resetField(TextField... field){
        for(TextField f : field){
            f.setStyle("");
            f.setText("");
        }
    }
}
