package ru.gordeev.demospringboot.repositories;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.gordeev.demospringboot.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class InMemoryProductRepository implements ProductsRepository {
    private List<Product> products;

    @PostConstruct
    public void init() {
        this.products = new ArrayList<>();
        products.add(new Product("Wheat Bread", 30.99));
        products.add(new Product("Rye Bread", 15.99));
        products.add(new Product("Butter 72%", 170.50));
        products.add(new Product("Butter 82%", 230.50));
        products.add(new Product("Milk 3%", 75.99));
        products.add(new Product("Milk 5%", 110.50));
        products.add(new Product("Soy Milk", 150.50));
        products.add(new Product("Chocolate",110.50));
        products.add(new Product("Child Chocolate",75.99));
        products.add(new Product("Fish", 230.50));
    }

    @Override
    public List<Product> getProducts() {
        return products.stream().toList();
    }

    @Override
    public Product getProductById(UUID id) {
        return products.stream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Product> getProductsByPrice(Double price) {
        return products.stream().filter(product -> product.getPrice().equals(price)).toList();
    }

    @Override
    public List<Product> getProductsByTitle(String title) {
        return products.stream().filter(product -> product.getTitle().equals(title)).toList();
    }

    @Override
    public Product createProduct(Product product) {
        products.add(product);
        return product;
    }
}
