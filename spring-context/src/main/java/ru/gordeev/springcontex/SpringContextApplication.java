package ru.gordeev.springcontex;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.gordeev.springcontex.service.CartService;

import java.util.Scanner;

@SpringBootApplication
public class SpringContextApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringContextApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            CartService cartService = context.getBean(CartService.class);
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.println("1. Add product to cart");
                    System.out.println("2. Remove product from cart");
                    System.out.println("3. Show cart");
                    System.out.println("4. Exit");

                    int choice = scanner.nextInt();
                    if (choice == 4) break;

                    switch (choice) {
                        case 1:
                            System.out.println("Enter product id to add:");
                            int idToAdd = scanner.nextInt();
                            cartService.addProductById(idToAdd);
                            break;
                        case 2:
                            System.out.println("Enter product id to remove:");
                            int idToRemove = scanner.nextInt();
                            cartService.removeProductById(idToRemove);
                            break;
                        case 3:
                            System.out.println("Cart contents:");
                            cartService.getProducts().forEach(System.out::println);
                            break;
                        default:
                            System.out.println("Invalid choice, please try again");
                    }
                }
            }
        };
    }

}
