package ru.gordeev.springboothibernate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gordeev.springboothibernate.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
