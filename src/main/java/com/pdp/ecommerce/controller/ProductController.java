package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public HttpEntity<?> getRandomProduct() {
        List<Product> products = productService.getRandomProducts();
        return ResponseEntity.ok(products);
    }
}
