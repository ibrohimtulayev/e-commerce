package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.model.dto.SearchDto;
import com.pdp.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/random")
    public HttpEntity<?> getRandomProduct() {
        return ResponseEntity.ok(productService.getRandomProducts());
    }

    @PostMapping("/search")
    public HttpEntity<?> findProduct(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(productService.findByNameAndGender(searchDto));
    }

    @GetMapping("/recommendation")
    public HttpEntity<?> getRecommendation() {
        return ResponseEntity.ok(productService.recommendProducts());
    }
}
