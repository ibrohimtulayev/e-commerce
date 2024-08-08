package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cart")
public class CartController {
    private final CartService cartService;
    @GetMapping
    public HttpEntity<?> cart() {
        return cartService.showAll();
    }
}
