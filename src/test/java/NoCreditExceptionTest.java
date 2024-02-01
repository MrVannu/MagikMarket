import org.junit.jupiter.api.Test;
import org.project.exceptions.NoCreditException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoCreditExceptionTest {

    int test = 0;

    @Test
    public void whenNoCredit_thenExceptionIsThrown() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            if (test <= 0){
                throw new NoCreditException("No credit Exception thrown");
            }
        });

        String expectedMessage = "No credit Exception thrown";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
