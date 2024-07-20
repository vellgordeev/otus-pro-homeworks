package ru.gordeev.springcontex.service;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.gordeev.springcontex.entities.Product;
import ru.gordeev.springcontex.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class Cart {
    @Getter
    private final List<Product> products = new ArrayList<>();
    private final ProductRepository productRepository;

    public Cart(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProductById(int id) {
        Product product = productRepository.findById(id);
        if (product != null) {
            products.add(product);
        } else {
            System.out.println("Product with id " + id + " not found");
        }
    }

    public void removeProductById(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (!removed) {
            System.out.println("Product with id " + id + " not found in cart");
        }
    }
}
