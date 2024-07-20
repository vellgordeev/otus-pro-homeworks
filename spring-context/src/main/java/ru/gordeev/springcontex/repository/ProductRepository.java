package ru.gordeev.springcontex.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.gordeev.springcontex.entities.Product;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

    @PostConstruct
    public void init() {
        products.add(new Product(1, "Product 1", 10.0));
        products.add(new Product(2, "Product 2", 20.0));
        products.add(new Product(3, "Product 3", 30.0));
        products.add(new Product(4, "Product 4", 40.0));
        products.add(new Product(5, "Product 5", 50.0));
        products.add(new Product(6, "Product 6", 60.0));
        products.add(new Product(7, "Product 7", 70.0));
        products.add(new Product(8, "Product 8", 80.0));
        products.add(new Product(9, "Product 9", 90.0));
        products.add(new Product(10, "Product 10", 100.0));
    }

    public List<Product> findAll() {
        return products;
    }

    public Product findById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }
}
