package ru.gordeev.crud_spring_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gordeev.crud_spring_project.dto.ProductRequestDto;
import ru.gordeev.crud_spring_project.dto.ProductResponseDto;
import ru.gordeev.crud_spring_project.exception.ProductNotFoundException;
import ru.gordeev.crud_spring_project.services.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id).orElseThrow(() -> new ProductNotFoundException(id)));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto savedProduct = productService.createProduct(productRequestDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, productRequestDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
