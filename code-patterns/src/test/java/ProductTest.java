import org.junit.jupiter.api.Test;
import ru.gordeev.Product;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreationWithAllFields() {
        Product product = Product.builder()
                .id(1)
                .title("Test Product")
                .description("Test product description")
                .cost(19.99)
                .weight(500)
                .width(30)
                .length(20)
                .height(10)
                .build();

        assertAll("product",
                () -> assertEquals(1, product.getId()),
                () -> assertEquals("Test Product", product.getTitle()),
                () -> assertEquals("A test product description", product.getDescription()),
                () -> assertEquals(19.99, product.getCost()),
                () -> assertEquals(500, product.getWeight()),
                () -> assertEquals(30, product.getWidth()),
                () -> assertEquals(20, product.getLength()),
                () -> assertEquals(10, product.getHeight())
        );
    }

    @Test
    void testProductCreationWithTwoFieldsOnly() {
        Product product = Product.builder()
                .id(2)
                .title("Minimal Product")
                .build();

        assertAll("product minimal",
                () -> assertEquals(2, product.getId()),
                () -> assertEquals("Minimal Product", product.getTitle()),
                () -> assertNull(product.getDescription()),
                () -> assertEquals(0.0, product.getCost()),
                () -> assertEquals(0, product.getWeight()),
                () -> assertEquals(0, product.getWidth()),
                () -> assertEquals(0, product.getLength()),
                () -> assertEquals(0, product.getHeight())
        );
    }

    @Test
    void testProductImmutability() {
        Product.Builder builder = Product.builder()
                .id(3)
                .title("Original Title");

        Product product1 = builder.build();
        builder.title("Changed Title");
        Product product2 = builder.build();

        assertNotEquals(product1.getTitle(), product2.getTitle());
    }
}
