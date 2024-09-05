package ru.gordeev.crud_spring_project.dto;

import lombok.Data;

@Data
public class ProductRequestDto {
    private String name;
    private Double price;
}
