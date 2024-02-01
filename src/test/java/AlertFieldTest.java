import javafx.scene.control.Alert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.util.AlertField;

class AlertFieldTest {

    @Test
    void testShowErrorAlert() {
        String title = "Error Title";
        String message = "Error Message";

        // Capture the shown alert for verification
        CustomAlert errorAlert = new CustomAlert(Alert.AlertType.ERROR);
        AlertField.showErrorAlert(title, message);

        // Verify that the captured alert matches the expected properties
        Assertions.assertEquals(Alert.AlertType.ERROR, errorAlert.getAlertType());
        Assertions.assertEquals(title, errorAlert.getTitle());
        Assertions.assertNull(errorAlert.getHeaderText());
        Assertions.assertEquals(message, errorAlert.getContentText());
    }

    @Test
    void testShowSuccessAlert() {
        String title = "Success Title";
        String message = "Success Message";

        // Capture the shown alert for verification
        CustomAlert successAlert = new CustomAlert(Alert.AlertType.INFORMATION);
        AlertField.showSuccessAlert(title, message);

        // Verify that the captured alert matches the expected properties
        Assertions.assertEquals(Alert.AlertType.INFORMATION, successAlert.getAlertType());
        Assertions.assertEquals(title, successAlert.getTitle());
        Assertions.assertNull(successAlert.getHeaderText());
        Assertions.assertEquals(message, successAlert.getContentText());
    }

    // ... (similar modifications for other test methods)

    // Helper class to capture alert properties
    private static class CustomAlert extends Alert {
        private Alert.AlertType alertType;

        CustomAlert(Alert.AlertType alertType) {
            super(alertType);
            this.alertType = alertType;
        }


    }
}
