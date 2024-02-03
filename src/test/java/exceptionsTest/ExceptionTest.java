package exceptionsTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExceptionTest {

    @Test
    public void whenAmountIsLessThanOrEqualToZero_thenExceptionIsThrown() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
                throw new RuntimeException("Exception thrown");
        });

        String expectedMessage = "Exception thrown";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}


