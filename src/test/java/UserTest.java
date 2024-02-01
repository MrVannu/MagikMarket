import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.project.controllers.LoginController;
import org.project.model.User;
import java.io.IOException;


public class UserTest {
    @Test
    public void testGetUsername() {
        User user = new User("john_doe"
        );
        String username = user.getUsername();
        assertEquals("john_doe", username);
    }

    @Test
    public void testGetUserCredit() throws IOException {
        User user = new User("testUsername"
        );

        LoginController obj = new LoginController();

        obj.registerNewUser("src/main/resources/userDB.csv", "testUsername", "hashedPassword",
                "user@example.com", 100.00);

        String userCredit = user.getUserCredit();
        assertEquals("100.0", userCredit);
    }

    @Test
    public void testGetUserCreditUserNotFound() {
        User user = new User("nonExistingUsername");
        RuntimeException exception = assertThrows(RuntimeException.class, user::getUserCredit);
        assertEquals("User not found into the DB", exception.getMessage());
    }

    @Test
    public void testSetUserCredit() throws IOException {
        User user = new User("test2Username"
        );

        LoginController obj = new LoginController();

        obj.registerNewUser("src/main/resources/userDB.csv", "test2Username", "hashedPassword",
                "user@example.com", 100.00);

        user.setUserCredit(150.0);

        String userCreditAfterUpdate = user.getUserCredit();
        assertEquals("150.0", userCreditAfterUpdate);
    }


}
