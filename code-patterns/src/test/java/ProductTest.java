import org.junit.jupiter.api.Test;
import ru.gordeev.iterator_builder.Product;

import static org.assertj.core.api.Assertions.*;

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

        assertThat(product).satisfies(p -> {
            assertThat(p.getId()).isEqualTo(1);
            assertThat(p.getTitle()).isEqualTo("Test Product");
            assertThat(p.getDescription()).isEqualTo("Test product description");
            assertThat(p.getCost()).isEqualTo(19.99);
            assertThat(p.getWeight()).isEqualTo(500);
            assertThat(p.getWidth()).isEqualTo(30);
            assertThat(p.getLength()).isEqualTo(20);
            assertThat(p.getHeight()).isEqualTo(10);
        });
    }

    @Test
    void testProductCreationWithTwoFieldsOnly() {
        Product product = Product.builder()
                .id(2)
                .title("Minimal Product")
                .build();

        assertThat(product).satisfies(p -> {
            assertThat(p.getId()).isEqualTo(2);
            assertThat(p.getTitle()).isEqualTo("Minimal Product");
            assertThat(p.getDescription()).isNull();
            assertThat(p.getCost()).isEqualTo(0.0);
            assertThat(p.getWeight()).isZero();
            assertThat(p.getWidth()).isZero();
            assertThat(p.getLength()).isZero();
            assertThat(p.getHeight()).isZero();
        });
    }

    @Test
    void testProductImmutability() {
        Product.Builder builder = Product.builder()
                .id(3)
                .title("Original Title");

        Product product1 = builder.build();
        builder.title("Changed Title");
        Product product2 = builder.build();

        assertThat(product1.getTitle()).isNotEqualTo(product2.getTitle());
    }
}