package ru.gordeev.crud_spring_project.validators;

import org.springframework.stereotype.Component;
import ru.gordeev.crud_spring_project.dto.ProductRequestDto;
import ru.gordeev.crud_spring_project.exception.InvalidProductException;

@Component
public class ProductValidator {

    public void validate(ProductRequestDto productRequestDto) {
        if (productRequestDto.getName() == null || productRequestDto.getName().trim().isEmpty()) {
            throw new InvalidProductException("Product name is required and cannot be empty");
        }
        if (productRequestDto.getPrice() == null || productRequestDto.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be greater than zero");
        }
    }
}
