
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.Stock;
import org.project.User;

public class test {

    User user = new User();
    @Test
    void myTest() {
        Assertions.assertTrue(!user.cartIsVoid());
    }
    @Test
    void addToCartTest(){
        Stock p = new Stock();
        user.addToCart(p);
        Assertions.assertTrue(user.getCart().contains(p));
    }
}
