package com.pdp.ecommerce.runner;

import com.github.javafaker.Faker;
import com.pdp.ecommerce.entity.*;
import com.pdp.ecommerce.entity.enums.GenderEnum;
import com.pdp.ecommerce.entity.enums.RoleEnum;
import com.pdp.ecommerce.service.*;
import com.pdp.ecommerce.service.aws.S3Service;
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
    private final ProductDetailsService productDetailsService;
    private final RatingService ratingService;
    private final CommentService commentService;
    private final Faker faker = new Faker();
    private final S3Service s3Service;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    private final Set<String> createdCategoryNames = new HashSet<>();

    @Override
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
        createCard("Visa", "1234567812345678", LocalDate.of(2025, 12, 31), 123);
        createCard("MasterCard", "9876543210987654", LocalDate.of(2026, 11, 30), 456);
    }

    private void createCard(String name, String cardNumber, LocalDate expiryDate, int cvv) {
        Card card = Card.builder()
                .name(name)
                .expiryDate(String.valueOf(expiryDate))
                .cardNumber(cardNumber)
                .cvv(cvv)
                .build();
        cardService.save(card);
    }

    private void generateCategoryProductAndProductDetails() {
        int totalProducts = 100;
        int categoriesCount = 5;
        int productsPerCategory = totalProducts / categoriesCount;

        for (int i = 0; i < categoriesCount; i++) {
            // Create parent category
            Category parentCategory = createCategory();
            for (int j = 0; j < 3; j++) {
                // Create subcategory and set its parent
                Category subCategory = createCategory();
                if (subCategory != null) {
                    subCategory.setParentCategoryId(parentCategory.getId());
                    categoryService.save(subCategory);  // Save subcategory
                    // Generate products for each subcategory
                    generateProducts(subCategory, productsPerCategory);
                }
            }
        }
    }

    private Category createCategory() {
        try {
            String categoryName;
            do {
                categoryName = faker.commerce().department();
            } while (createdCategoryNames.contains(categoryName));

            createdCategoryNames.add(categoryName);
            Category savedCategory = Category.builder()
                    .id(UUID.randomUUID())
                    .name(categoryName)
                    .image("https://my-bucket-personal-aws.s3.eu-north-1.amazonaws.com/2024-08-13/category.jpg")
                    .build();

            return categoryService.save(savedCategory);
        } catch (Exception e) {
            return null; // Handle exception appropriately
        }
    }

    private void generateProducts(Category category, int count)   {
        for (int i = 0; i < count; i++) {
            List<ProductDetails> productDetailsList = createProductDetailsList();
            List<ProductDetails> savedProductDetailsList = productDetailsService.saveAll(productDetailsList);
            Product product = Product.builder()
                    .id(UUID.randomUUID())
                    .name(faker.commerce().productName())
                    .description(faker.lorem().sentence())
                    .category(category)
                    .productDetails(savedProductDetailsList)
                    .image("https://my-bucket-personal-aws.s3.eu-north-1.amazonaws.com/2024-08-13/product.jpg")
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
                "#FF5733", "#33FF57", "#3357FF"
        );
        List<String> sizes = Arrays.asList("L", "XL", "XXL");

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
                        .description(faker.hipster().word())
                        .build();
                commentService.save(comment);
            }
        }
    }
}