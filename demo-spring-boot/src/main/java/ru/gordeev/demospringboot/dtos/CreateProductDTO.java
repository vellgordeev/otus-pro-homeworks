package ru.gordeev.demospringboot.dtos;

import lombok.Data;

@Data
public class CreateProductDTO {
    private String title;
    private Double price;
}
