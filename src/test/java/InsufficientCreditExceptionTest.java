import org.junit.jupiter.api.Test;
import org.project.exceptions.InsufficientCreditException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InsufficientCreditExceptionTest {

    @Test
    public void whenInsufficientCredit_thenExceptionIsThrown() {
        InsufficientCreditException exception = assertThrows(InsufficientCreditException.class, () -> {
            throw new InsufficientCreditException();
        });

        assertTrue(exception.isAlertShown());
    }
}
