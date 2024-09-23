package ru.gordeev.crud_spring_project.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.gordeev.crud_spring_project.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
