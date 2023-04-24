
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.Product;
import org.project.User;

public class test {

    User user = new User();
    @Test
    void myTest() {
        Assertions.assertTrue(!user.cartIsVoid());
    }
    @Test
    void addToCartTest(){
        Product p = new Product();
        user.addToCart(p);
        Assertions.assertTrue(user.getCart().contains(p));
    }
}
