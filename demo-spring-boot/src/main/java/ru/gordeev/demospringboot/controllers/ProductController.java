package ru.gordeev.demospringboot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gordeev.demospringboot.dtos.CreateProductDTO;
import ru.gordeev.demospringboot.entities.Product;
import ru.gordeev.demospringboot.services.ProductService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.findById(id);
    }

    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) Double price,
                                     @RequestParam(required = false) String title) {
        if (price != null) {
            return productService.findByPrice(price);
        } else if (title != null) {
            return productService.findByTitle(title);
        } else {
            throw new IllegalArgumentException("Either price or title must be provided");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody CreateProductDTO createProductDTO) {
        if (createProductDTO != null &&
                createProductDTO.getTitle() != null &&
                !createProductDTO.getTitle().isEmpty() &&
                createProductDTO.getPrice() != null) {
            return productService.createProduct(createProductDTO);
        } else {
            throw new IllegalArgumentException("Price and title must be provided");
        }
    }
}
