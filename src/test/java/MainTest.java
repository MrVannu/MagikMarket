import org.junit.jupiter.api.Test;
import org.project.Main;

import static org.junit.jupiter.api.Assertions.*;


public class MainTest {
    private Main test = new Main();

    @Test
    void testValidUsername() {
        String validUsername = "john_doe";
        assertTrue(test.usernameValidator(validUsername));
    }

    @Test
    void testInvalidUsernameWithSpace() {
        String invalidUsername = "user name";
        assertFalse(test.usernameValidator(invalidUsername));
    }

    @Test
    void testInvalidEmptyUsername() {
        String invalidUsername = "";
        assertFalse(test.usernameValidator(invalidUsername));
    }

    @Test
    void testValidEmail() {
        String validEmail = "example@example.com";
        assertTrue(test.emailValidator(validEmail));
    }

    @Test
    void testInvalidEmail() {
        String invalidEmail = "invalid_email";
        assertFalse(test.emailValidator(invalidEmail));
    }

    @Test
    void testValidPassword() {
        String validPassword = "SecurePassword123";
        assertTrue(test.passwordValidator(validPassword));
    }

    @Test
    void testEmptyPassword() {
        String emptyPassword = "";
        assertFalse(test.passwordValidator(emptyPassword));
    }

    @Test
    void testValidFields() {
        String validUsername = "john_doe";
        String validPassword = "SecurePassword123";
        String validEmail = "example@example.com";
        String hashedPassword = "hashed_password";

        assertTrue(test.globalValidator(validUsername, validPassword, hashedPassword, validEmail));
    }

    @Test
    void testInvalidUsername() {
        String invalidUsername = "user name";
        String validPassword = "SecurePassword123";
        String validEmail = "example@example.com";
        String hashedPassword = "hashed_password";

        assertFalse(test.globalValidator(invalidUsername, validPassword, hashedPassword, validEmail));
    }

    @Test
    void testInvalidPassword() {
        String validUsername = "john_doe";
        String invalidPassword = "";
        String validEmail = "example@example.com";
        String hashedPassword = "hashed_password";

        assertFalse(test.globalValidator(validUsername, invalidPassword, hashedPassword, validEmail));
    }

    @Test
    void testInvalidEmailGlobally() {
        String validUsername = "john_doe";
        String validPassword = "SecurePassword123";
        String invalidEmail = "invalid_email";
        String hashedPassword = "hashed_password";

        assertFalse(test.globalValidator(validUsername, validPassword, hashedPassword, invalidEmail));
    }
}
