package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.*;
import com.pdp.ecommerce.repository.BasketProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketProductServiceImpl implements BasketProductService {
    private final BasketProductRepository basketProductRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final UserService userService;
    private final DiscountService discountService;

    @Override
    @Transactional
    public HttpEntity<?> add(UUID productDetailsId, Integer amount) {
        ProductDetails productDetails = productDetailsRepository.findById(productDetailsId)
                .orElseThrow(() -> new RuntimeException("Product details not found"));
        User user = userService.getSignedUser()
                .orElseThrow(() -> new RuntimeException("User not found"));





        BasketProduct basketProduct = BasketProduct.builder()
                .productDetails(productDetails)
                .user(user)
                .amount(amount)
                .build();
        return ResponseEntity.ok(basketProductRepository.save(basketProduct));
    }

    @Override
    public HttpEntity<?> remove(UUID id) {
        basketProductRepository.deleteById(id);
        return ResponseEntity.ok("success");
    }

    @Override
    public List<BasketProduct> userBasketProduct() {
        User user = userService.getSignedUser().orElseThrow();
        return basketProductRepository.userBasketProducts(user.getId());
    }
}
