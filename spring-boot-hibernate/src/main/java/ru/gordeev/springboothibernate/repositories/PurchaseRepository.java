package ru.gordeev.springboothibernate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gordeev.springboothibernate.entities.Purchase;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByProductId(Long productId);
    List<Purchase> findByCustomerId(Long customerId);
}

