package ru.gordeev.demospringboot.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.gordeev.demospringboot.dtos.CreateProductDTO;
import ru.gordeev.demospringboot.entities.Product;
import ru.gordeev.demospringboot.repositories.ProductsRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductsRepository productsRepository;

    public List<Product> findAll() {
        return productsRepository.getProducts();
    }

    public Product findById(String id) {
        return productsRepository.getProductById(UUID.fromString(id));
    }

    public List<Product> findByPrice(Double price) {
        return productsRepository.getProductsByPrice(price);
    }

    public List<Product> findByTitle(String title) {
        return productsRepository.getProductsByTitle(title);
    }

    public Product createProduct(CreateProductDTO createProductDTO) {
        Product createdProduct = productsRepository.createProduct(
                new Product(createProductDTO.getTitle(), createProductDTO.getPrice())
        );
        logger.info("created product: {}", createdProduct);
        return createdProduct;
    }
}
