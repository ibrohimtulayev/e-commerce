package com.pdp.ecommerce.runner;

import com.github.javafaker.Faker;
import com.pdp.ecommerce.entity.*;
import com.pdp.ecommerce.entity.enums.GenderEnum;
import com.pdp.ecommerce.entity.enums.RoleEnum;
import com.pdp.ecommerce.repository.ProductDetailsService;
import com.pdp.ecommerce.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserService userService;
    private final CardService cardService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final Faker faker = new Faker();
    private final ProductDetailsService productDetailsService;
    private final RatingService ratingService;
    private final CommentService commentService;


    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    @Transactional
    public void run(String... args) {
        if (ddl.equals("create")) {
            generateRoleUserAndCards();
            generateCategoryProductAndProductDetails();

        }
    }

    private void generateRoleUserAndCards() {
        Role adminRole = Role.builder()
                .name(RoleEnum.ROLE_ADMIN)
                .build();

        Role userRole = Role.builder()
                .name(RoleEnum.ROLE_USER)
                .build();

        roleService.save(adminRole);
        roleService.save(userRole);

        User admin = User.builder()
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("1221"))
                .roles(List.of(adminRole))
                .build();
        userService.save(admin);

        for (int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .email("user%d@gmail.com".formatted(i))
                    .password(passwordEncoder.encode("1221"))
                    .roles(List.of(userRole))
                    .build();

            userService.save(user);
        }

        // Create Cards
        Card card1 = Card.builder().name("Visa").expiryDate(LocalDate.of(2025, 12, 31))
                .cardNumber("1234567812345678").cvv(123).build();
        Card card2 = Card.builder().name("MasterCard").expiryDate(LocalDate.of(2026, 11, 30))
                .cardNumber("9876543210987654").cvv(456).build();
        cardService.save(card1);
        cardService.save(card2);


    }

    private void generateCategoryProductAndProductDetails() {
        for (int i = 0; i < 5; i++) {
            Category parentCategory = createCategory();
            categoryService.save(parentCategory);

            for (int j = 0; j < 5; j++) {
                Category subCategory = createCategory();
                subCategory.setParentCategoryId(parentCategory.getId());
                Category savedSubCategory = categoryService.save(subCategory);

                // Generate products for each subcategory
                generateProducts(savedSubCategory);
            }
        }
    }

    private Category createCategory() {
        return Category.builder()
                .id(UUID.randomUUID())
                .name(faker.commerce().department())
                .build();
    }

    private void generateProducts(Category category) {
        for (int i = 0; i < 40; i++) {
            List<ProductDetails> productDetailsList = createProductDetailsList();
            List<ProductDetails> savedProductDetailsList = productDetailsService.saveAll(productDetailsList);

            Product product = Product.builder()
                    .id(UUID.randomUUID())
                    .name(faker.commerce().productName())
                    .description(faker.lorem().sentence())
                    .category(category)
                    .productDetails(savedProductDetailsList)
                    .build();

            Product savedProduct = productService.save(product);
            List<User> users = userService.findAllUsersByRole(RoleEnum.ROLE_USER.name());

            generateRating(savedProduct, users);
            generateComments(savedProduct, users);
        }
    }

    private List<ProductDetails> createProductDetailsList() {
        List<ProductDetails> productDetailsList = new ArrayList<>();
        List<String> colors = Arrays.asList(
                "#FF5733", // Red-Orange
                "#33FF57", // Green
                "#3357FF", // Blue
                "#F0F0F0", // Light Gray
                "#FF33A6", // Pink
                "#33FFF6", // Cyan
                "#F5FF33", // Yellow
                "#FF6F33", // Orange
                "#6A33FF", // Purple
                "#33FF6A"  // Light Green
        );
        List<String> sizes = Arrays.asList(
                "XS",   // Extra Small
                "S",    // Small
                "M",    // Medium
                "L",    // Large
                "XL",   // Extra Large
                "XXL"  // Double Extra Large
        );
        for (String color : colors) {
            for (String size : sizes) {
                ProductDetails productDetails = ProductDetails.builder()
                        .size(size)
                        .color(color)
                        .gender(GenderEnum.values()[faker.number().numberBetween(0, GenderEnum.values().length)])
                        .quantity(faker.number().numberBetween(5, 10))
                        .price(Double.parseDouble(faker.commerce().price()))
                        .build();
                productDetailsList.add(productDetails);
            }
        }

        return productDetailsList;
    }

    private void generateRating(Product product, List<User> users) {
        Random random = new Random();
        for (User user : users) {
            Rating rating = Rating.builder()
                    .grade(random.nextInt(1, 6))
                    .product(product)
                    .user(user)
                    .build();
            ratingService.save(rating);
        }
    }


    private void generateComments(Product product, List<User> users) {
        for (User user : users) {
            for (int i = 0; i < 3; i++) {
                Comment comment = Comment.builder()
                        .product(product)
                        .user(user)
                        .description(faker.lorem().characters(10, 15))
                        .build();
                commentService.save(comment);
            }
        }
    }
}


