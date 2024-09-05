package ru.gordeev.crud_spring_project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
