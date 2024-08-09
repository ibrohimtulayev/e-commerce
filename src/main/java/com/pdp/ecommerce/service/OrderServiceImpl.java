package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.*;
import com.pdp.ecommerce.model.dto.OrderDto;
import com.pdp.ecommerce.model.projection.UserOrderProjection;
import com.pdp.ecommerce.repository.BasketProductRepository;
import com.pdp.ecommerce.repository.OrderProductRepository;
import com.pdp.ecommerce.repository.OrderRepository;
import com.pdp.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CardService cardService;
    private final ProductDetailsService productDetailsService;
    private final OrderProductRepository orderProductRepository;
    private final PaymentRepository paymentRepository;
    private final BasketProductRepository basketProductRepository;

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public HttpEntity<?> deliveryDestination() {
        Optional<User> signedUser = userService.getSignedUser();
        if (signedUser.isPresent()) {
            return ResponseEntity.ok(signedUser.get().getAddress());
        }
        else throw new RuntimeException("user not found");
    }

    @Override
    @Transactional
    public HttpEntity<?> make(OrderDto orderDto) {
        String cardNUmber = orderDto.cardNumber();
        Card card = cardService.findByNumber(cardNUmber);
        if (!cardService.isCardExpired(card.getExpiryDate())&&card.getBalance()>=orderDto.orderPrice()) {
            Order order = Order.builder()
                    .user(userService.getSignedUser().orElseThrow(()->new RuntimeException("user not found")))
                    .deliveryTime(LocalDateTime.now().plusDays(2))
                    .build();
            orderRepository.save(order);
            List<Map<Integer, UUID>> maps = orderDto.productDetailIdWithAmount();
            for (Map<Integer, UUID> map : maps) {
                for (Map.Entry<Integer, UUID> entry : map.entrySet()) {
                    Integer amount = entry.getKey();
                    UUID basketProductId = entry.getValue();
                    Optional<BasketProduct> byId = basketProductRepository.findById(basketProductId);
                    UUID productDetailId = null;
                    if (byId.isPresent()) {
                        BasketProduct basketProduct = byId.get();
                        productDetailId = basketProduct.getProductDetails().getId();
                    }
                    ProductDetails productDetails = productDetailsService.findById(productDetailId)
                            .orElseThrow(() -> new RuntimeException("ProductDetails not found"));

                    OrderProduct orderProduct = OrderProduct.builder()
                            .productDetails(productDetails)
                            .amount(amount)
                            .order(order)
                            .build();
                    orderProductRepository.save(orderProduct);

                }
            }
            card.setBalance(card.getBalance() - orderDto.orderPrice());
            cardService.save(card);
            Payment payment = Payment.builder()
                    .order(order)
                    .user(userService.getSignedUser().orElseThrow(()->new RuntimeException("user not found")))
                    .amount(orderDto.orderPrice())
                    .card(card)
                    .build();
            paymentRepository.save(payment);
            return ResponseEntity.ok("Order created successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("something went wrong");
    }


    @Override
    public HttpEntity<?> findByUserOrders(UUID id) {
        List<UserOrderProjection> orderByUser = orderRepository.findOrderByUser(id);
        return ResponseEntity.ok(orderByUser);
    }



}
