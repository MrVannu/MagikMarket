import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmountNotAllowedTest {
    int test = 0;

    @Test
    public void whenAmountIsLessThanOrEqualToZero_thenExceptionIsThrown() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            if (test <= 0) {
                throw new RuntimeException("Amount must be greater than 0");
            }
        });

        String expectedMessage = "Amount must be greater than 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}


