package ru.gordeev.springboothibernate.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.gordeev.springboothibernate.entities.Customer;
import ru.gordeev.springboothibernate.entities.Product;
import ru.gordeev.springboothibernate.entities.Purchase;
import ru.gordeev.springboothibernate.services.CustomerService;
import ru.gordeev.springboothibernate.services.ProductService;
import ru.gordeev.springboothibernate.services.PurchaseService;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleApplication implements CommandLineRunner {

    private final CustomerService customerService;
    private final ProductService productService;
    private final PurchaseService purchaseService;

    @Autowired
    public ConsoleApplication(CustomerService customerService, ProductService productService, PurchaseService purchaseService) {
        this.customerService = customerService;
        this.productService = productService;
        this.purchaseService = purchaseService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. View all customers");
            System.out.println("2. View all products");
            System.out.println("3. View all purchases");
            System.out.println("4. View products purchased by a customer");
            System.out.println("5. View customers who purchased a product");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewAllCustomers();
                    break;
                case 2:
                    viewAllProducts();
                    break;
                case 3:
                    viewAllPurchases();
                    break;
                case 4:
                    viewProductsByCustomer();
                    break;
                case 5:
                    viewCustomersByProduct();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private void viewAllProducts() {
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void viewAllPurchases() {
        List<Purchase> purchases = purchaseService.getAllPurchases();
        for (Purchase purchase : purchases) {
            System.out.println(purchase);
        }
    }

    private void viewProductsByCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer ID: ");
        Long customerId = scanner.nextLong();
        List<Product> products = purchaseService.getProductsByCustomerId(customerId);
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void viewCustomersByProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID: ");
        Long productId = scanner.nextLong();
        List<Purchase> purchases = purchaseService.getPurchasesByProductId(productId);
        for (Purchase purchase : purchases) {
            System.out.println(purchase.getCustomer());
        }
    }
}
