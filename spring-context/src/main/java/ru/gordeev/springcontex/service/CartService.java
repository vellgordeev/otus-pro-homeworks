package ru.gordeev.springcontex.service;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.gordeev.springcontex.entities.Product;
import ru.gordeev.springcontex.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final Logger logger = LogManager.getLogger(CartService.class);

    @Getter
    private final List<Product> products = new ArrayList<>();
    private final ProductRepository productRepository;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            products.add(product.get());
        } else {
            logger.error("Product with id {} not found", id);
        }
    }

    public void removeProductById(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (!removed) {
            logger.error("Product with id {} not found in cart", id);
        }
    }
}
