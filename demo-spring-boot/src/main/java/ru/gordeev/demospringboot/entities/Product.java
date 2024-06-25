package ru.gordeev.demospringboot.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class Product {
    private UUID id;
    private String title;
    private Double price;

    public Product(String title, Double price) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.price = price;
    }
}