package ru.gordeev.springboothibernate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gordeev.springboothibernate.entities.Product;
import ru.gordeev.springboothibernate.entities.Purchase;
import ru.gordeev.springboothibernate.repositories.PurchaseRepository;

import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }

    public List<Purchase> getPurchasesByProductId(Long productId) {
        return purchaseRepository.findByProductId(productId);
    }

    public List<Purchase> getPurchasesByCustomerId(Long customerId) {
        return purchaseRepository.findByCustomerId(customerId);
    }

    public List<Product> getProductsByCustomerId(Long customerId) {
        List<Purchase> purchases = purchaseRepository.findByCustomerId(customerId);
        return purchases.stream()
                .map(Purchase::getProduct)
                .toList();
    }
}
