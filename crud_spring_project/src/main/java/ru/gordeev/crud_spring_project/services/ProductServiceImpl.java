package ru.gordeev.crud_spring_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gordeev.crud_spring_project.dto.ProductRequestDto;
import ru.gordeev.crud_spring_project.dto.ProductResponseDto;
import ru.gordeev.crud_spring_project.entities.Product;
import ru.gordeev.crud_spring_project.exception.ProductNotFoundException;
import ru.gordeev.crud_spring_project.repositories.ProductRepository;
import ru.gordeev.crud_spring_project.validators.ProductValidator;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    @Override
    public Iterable<ProductResponseDto> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponseDto> getProductById(Long id) {
        return Optional.ofNullable(productRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ProductNotFoundException(id)));
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        productValidator.validate(productRequestDto);
        Product product = convertToEntity(productRequestDto);

        if (productRequestDto.getName() != null)
            product.setName(productRequestDto.getName());
        if (productRequestDto.getPrice() != null)
            product.setPrice(productRequestDto.getPrice());

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    @Override
    public Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto productRequestDto) {
        productValidator.validate(productRequestDto);
        return productRepository.findById(id).map(productToUpdate -> {
            productToUpdate.setName(productRequestDto.getName());
            productToUpdate.setPrice(productRequestDto.getPrice());
            Product updatedProduct = productRepository.save(productToUpdate);
            return Optional.of(convertToDto(updatedProduct));
        }).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.deleteById(id);
    }

    private ProductResponseDto convertToDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setCreatedAt(product.getCreatedAt());
        productResponseDto.setUpdatedAt(product.getUpdatedAt());
        return productResponseDto;
    }

    private Product convertToEntity(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        return product;
    }
}


