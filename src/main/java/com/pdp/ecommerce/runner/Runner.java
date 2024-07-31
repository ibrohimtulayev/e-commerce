package com.pdp.ecommerce.runner;

import com.pdp.ecommerce.entity.*;
import com.pdp.ecommerce.entity.enums.GenderEnum;
import com.pdp.ecommerce.entity.enums.OrderStatus;
import com.pdp.ecommerce.entity.enums.RoleName;
import com.pdp.ecommerce.repository.UserRepository;
import com.pdp.ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserService userService;
    private final CardService cardService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final DiscountService discountService;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final OrderProductService orderProductService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        if (ddl.equals("create")) {

            Role adminRole = Role.builder()
                    .roleName(RoleName.ROLE_ADMIN)
                    .build();

            Role userRole = Role.builder()
                    .roleName(RoleName.ROLE_USER)
                    .build();

            roleService.save(adminRole);
            roleService.save(userRole);




            User user = User.builder()
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("1221"))
                    .roles(List.of(userRole))
                    .build();

            userService.save(user);


            // Create Addresses
            Address address1 = Address.builder().latitude(34.0522).longitude(-118.2437).build();
            Address address2 = Address.builder().latitude(40.7128).longitude(-74.0060).build();
            addressService.save(address1);
            addressService.save(address2);

            // Create Cards
            Card card1 = Card.builder().name("Visa").expiryDate(LocalDate.of(2025, 12, 31))
                    .cardNumber("1234567812345678").cvv(123).build();
            Card card2 = Card.builder().name("MasterCard").expiryDate(LocalDate.of(2026, 11, 30))
                    .cardNumber("9876543210987654").cvv(456).build();
            cardService.save(card1);
            cardService.save(card2);

            // Create Categories
            Category category1 = Category.builder().name("Clothing").build();
            Category category2 = Category.builder().name("Accessories").parentCategoryId(category1.getId()).build();
            categoryService.save(category1);
            categoryService.save(category2);


            // Create Products
            Product product1 = Product.builder()
                    .name("T-Shirt")
                    .description("Cotton T-Shirt")
                    .productDetails(ProductDetails
                            .builder()
                            .size("M")
                            .price(19.99)
                            .quantity(10)
                            .color("RED")
                            .gender(GenderEnum.MALE)
                            .build())
                    .category(category1)

                    .build();
            Product product2 = Product.builder()
                    .name("T-Shirt")
                    .description("Cotton T-Shirt")
                    .productDetails(ProductDetails.builder()
                            .size("L")
                            .price(19.99)
                            .color("BLUE")
                            .quantity(20)
                            .gender(GenderEnum.MALE)
                            .build())
                    .category(category1)
                    .build();

            Product product3 = Product.builder()
                    .name("Watch")
                    .description("Stylish wristwatch")
                    .productDetails(ProductDetails.builder()
                            .size("50")
                            .color("WHITE")
                            .gender(GenderEnum.FEMALE)
                            .quantity(5)
                            .price(49.99)
                            .build())
                    .category(category2)
                    .build();
            productService.save(product1);
            productService.save(product2);
            productService.save(product3);

            User admin = User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("1221"))
                    .roles(List.of(adminRole))
                    .favouriteProducts(List.of(product1))
                    .build();
            userRepository.save(admin);

            // Create Discounts
            Discount discount = Discount.builder().startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(1)).amount(10).description("Summer Sale").build();
            discountService.save(discount);

            // Create Comments
            Comment comment1 = Comment.builder().description("Great product!").user(user).build();
            Comment comment2 = Comment.builder().description("Not bad!").user(user).build();
            commentService.save(comment1);
            commentService.save(comment2);

            // Create Ratings
            Rating rating1 = Rating.builder().grade(5).user(user).build();
            ratingService.save(rating1);

            // Create Orders
            Order order = Order.builder().user(user).status(OrderStatus.PENDING).deliveryTime(LocalDateTime.now().plusDays(3)).build();
            orderService.save(order);

            // Create Payments
            Payment payment = Payment.builder().user(user).amount(69.98).order(order).card(card1).build();
            paymentService.save(payment);

            // Associate Products with Order
            OrderProduct orderProduct1 = OrderProduct.builder().product(product1).amount(1).order(order).build();
            OrderProduct orderProduct2 = OrderProduct.builder().product(product2).amount(1).order(order).build();
            // Assuming you have an OrderProductService to save OrderProducts
            orderProductService.save(orderProduct1);
            orderProductService.save(orderProduct2);
        }
    }
}


