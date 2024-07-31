package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/discount")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;
    @GetMapping
    public HttpEntity<?> getDiscount(){
        return discountService.getDiscountEvent();
    }
}
