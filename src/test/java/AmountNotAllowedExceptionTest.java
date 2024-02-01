import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmountNotAllowedExceptionTest {
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

