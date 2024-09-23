package ru.gordeev.crud_spring_project.services;

import ru.gordeev.crud_spring_project.dto.ProductRequestDto;
import ru.gordeev.crud_spring_project.dto.ProductResponseDto;

import java.util.Optional;

public interface ProductService {
    Iterable<ProductResponseDto> getAllProducts();
    Optional<ProductResponseDto> getProductById(Long id);
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto productRequestDto);
    void deleteProduct(Long id);
}
