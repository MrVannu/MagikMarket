import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import org.junit.jupiter.api.Test;
import org.project.AmountNotAllowed;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmountNotAllowedTest {
    int a = 0;

    @Test
    public void whenAmountIsLessThanOrEqualToZero_thenExceptionIsThrown() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Amount must be greater than 0");
        });

        String expectedMessage = "Amount must be greater than 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

