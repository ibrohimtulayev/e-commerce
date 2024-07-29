package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.model.dto.SearchDto;
import com.pdp.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public HttpEntity<?> getRandomProduct() {
        List<Product> products = productService.getRandomProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public HttpEntity<?> findProduct(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(productService.findByNameAndGender(searchDto));
    }

}
