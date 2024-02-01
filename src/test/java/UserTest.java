import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.project.NoCreditException;
import org.project.User;

import java.io.FileWriter;
import java.io.IOException;
/*
@Test
public void testUserNotFound() {
        User user = new User("nonExistingUsername", "password", "hashedPassword",
        "user@example.com", 100.0);
        assertThrows(RuntimeException.class, user::getUserCredit);
        }

 */
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserTest {

    private static final String TEST_DB_PATH = "LoginControlTestDB.csv";

    @BeforeEach
    void setUp() {
        clearTestDB();// Initialize or clear the test CSV file
    }

    @Test
    public void testGetUsername() {
        User user = new User("john_doe", "password123", "hashedPassword",
                "john.doe@example.com", 50.0);
        String username = user.getUsername();
        assertEquals("john_doe", username);
    }

    @Test
    public void testGetUserCredit() {
        User user = new User("existingUsername", "password", "hashedPassword",
                "user@example.com", 100.0);
        String userCredit = user.getUserCredit();
        assertEquals("100.0", userCredit);
    }

    @Test
    public void testGetUserCreditUserNotFound() {
        User user = new User("nonExistingUsername", "password", "hashedPassword", "user@example.com", 100.0);
        RuntimeException exception = assertThrows(RuntimeException.class, user::getUserCredit);
        assertEquals("User not found into the DB", exception.getMessage());
    }

    @Test
    public void testSetUserCredit() throws IOException {
        // Arrange
        User user = new User("existingUsername", "password", "hashedPassword",
                "user@example.com", 100.0);

        // Act
        user.setUserCredit(150.0);

        // Assert
        String userCreditAfterUpdate = user.getUserCredit();
        assertEquals("150.0", userCreditAfterUpdate);
    }

    @Test
    public void testSetUserCreditWithNoCreditException() {
        // Arrange
        User user = new User("existingUsername", "password", "hashedPassword", "user@example.com", 100.0);

        // Act and Assert
        NoCreditException exception = assertThrows(NoCreditException.class, () -> user.setUserCredit(-50.0));
        assertEquals("User has insufficient credit", exception.getMessage());
    }

    private void clearTestDB() {
        // Clears the test CSV file
        Path testDBPath = Paths.get(TEST_DB_PATH);
        try {
            Files.deleteIfExists(testDBPath);
            Files.createFile(testDBPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
