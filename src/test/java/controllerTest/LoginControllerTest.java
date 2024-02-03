package controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.project.controllers.LoginController;

import java.io.IOException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {
    @BeforeEach
    void setUp() {
        clearTestDB();// Initialize or clear the test CSV file
    }

    @Test
    public void testUsernameExists() throws IOException {
        // Create a temporary CSV file for testing
        String testCsvPath = "LoginControlTestDB.csv";
        createTestCsvUsername(testCsvPath);

        // Creating an instance of YourClass (replace with the actual class name)
        LoginController myClass = new LoginController();

        // Test cases
        assertTrue(myClass.usernameExists("existingUser", testCsvPath));
        assertTrue(myClass.usernameExists("anotherUser", testCsvPath));
        assertFalse(myClass.usernameExists("nonexistentUser", testCsvPath));
    }

    // Helper method to create a test CSV file
    private void createTestCsvUsername(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("existingUser,password123\n");
            writer.write("anotherUser,password456\n");
        }
    }

    @Test
    public void testPasswordCorresponds() throws IOException {
        // Create a temporary CSV file for testing
        String testCsvPath = "LoginControlTestDB.csv";
        createTestCsvPassword(testCsvPath);

        // Creating an instance of YourClass (replace with the actual class name)
         LoginController myClass = new LoginController();

        // Test cases
        assertTrue(myClass.passwordCorresponds("existingUser", "password123", testCsvPath));
        assertTrue(myClass.passwordCorresponds("anotherUser", "password456", testCsvPath));
        assertFalse(myClass.passwordCorresponds("existingUser", "wrongPassword", testCsvPath));
        assertFalse(myClass.passwordCorresponds("nonexistentUser", "password789", testCsvPath));
    }

    // Helper method to create a test CSV file
    private void createTestCsvPassword(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Use BCrypt to hash the passwords before writing to the file
            writer.write("existingUser," + BCrypt.hashpw("password123", BCrypt.gensalt()) + "\n");
            writer.write("anotherUser," + BCrypt.hashpw("password456", BCrypt.gensalt()) + "\n");
        }
    }

    @Test
    public void testCheckRegexMatch() {
        // Creating an instance of LoginControl
        LoginController myClass = new LoginController();

        // Test cases
        assertTrue(myClass.checkRegexMatch("\\d{3}-\\d{2}-\\d{4}", "123-45-6789")); // Valid SSN
        assertTrue(myClass.checkRegexMatch("[A-Za-z]+", "OnlyLetters")); // Only letters
        assertFalse(myClass.checkRegexMatch("\\d{3}-\\d{2}-\\d{4}", "InvalidSSN")); // Invalid SSN
        assertFalse(myClass.checkRegexMatch("[A-Za-z]+", "123")); // Invalid, only digits
        assertTrue(myClass.checkRegexMatch("[0-9]+", "123")); // Valid, only digits
    }

    @Test
    public void testValidUsername() {
        String validUsername = "JohnDoe";
        boolean isValid = new LoginController().usernameValidator(validUsername);
        assertTrue(isValid);
    }

    @Test
    public void testInvalidUsernameWithSpace() {
        String invalidUsername = "John Doe";
        boolean isValid = new LoginController().usernameValidator(invalidUsername);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidUsernameEmpty() {
        String invalidUsername = "";
        boolean isValid = new LoginController().usernameValidator(invalidUsername);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidUsernameNull() {
        boolean isValid = new LoginController().usernameValidator(null);
        assertFalse(isValid);
    }

    @Test
    public void testValidEmail() {
        String validEmail = "john.doe@example.com";
        boolean isValid = new LoginController().emailValidator(validEmail);
        assertTrue(isValid);
    }

    @Test
    public void testInvalidEmailNoAtSymbol() {
        String invalidEmail = "john.doeexample.com";
        boolean isValid = new LoginController().emailValidator(invalidEmail);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidEmailNoDot() {
        String invalidEmail = "john.doe@examplecom";
        boolean isValid = new LoginController().emailValidator(invalidEmail);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidEmailNoUsername() {
        String invalidEmail = "@example.com";
        boolean isValid = new LoginController().emailValidator(invalidEmail);
        assertFalse(isValid);
    }

    @Test
    public void testInvalidEmailNoDomain() {
        String invalidEmail = "john.doe@.com";
        boolean isValid = new LoginController().emailValidator(invalidEmail);
        assertFalse(isValid);
    }




    private void clearTestDB() {
        // Clears the test CSV file
        Path testDBPath = Paths.get("LoginControlTest.java");
        try {
            Files.deleteIfExists(testDBPath);
            Files.createFile(testDBPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
