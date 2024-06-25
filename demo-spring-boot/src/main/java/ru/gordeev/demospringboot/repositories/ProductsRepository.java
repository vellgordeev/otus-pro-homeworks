package ru.gordeev.demospringboot.repositories;

import ru.gordeev.demospringboot.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductsRepository {
    Product getProductById(UUID id);
    List<Product> getProductsByPrice(Double price);
    List<Product> getProducts();
    List<Product> getProductsByTitle(String category);
    Product createProduct(Product product);
}
