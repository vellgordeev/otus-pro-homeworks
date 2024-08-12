package ru.gordeev.springboothibernate.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.gordeev.springboothibernate.services.CustomerService;
import ru.gordeev.springboothibernate.services.ProductService;
import ru.gordeev.springboothibernate.services.PurchaseService;

import java.util.Scanner;

@Component
public class ConsoleApplication implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);

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
        customerService.getAllCustomers().forEach(System.out::println);
    }

    private void viewAllProducts() {
        productService.getAllProducts().forEach(System.out::println);
    }

    private void viewAllPurchases() {
        purchaseService.getAllPurchases().forEach(System.out::println);
    }

    private void viewProductsByCustomer() {
        System.out.print("Enter customer ID: ");
        Long customerId = scanner.nextLong();

        purchaseService.getProductsByCustomerId(customerId).forEach(System.out::println);
    }

    private void viewCustomersByProduct() {
        System.out.print("Enter product ID: ");
        Long productId = scanner.nextLong();

        purchaseService.getPurchasesByProductId(productId).forEach(System.out::println);
    }
}
