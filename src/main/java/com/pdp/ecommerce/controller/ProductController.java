package com.pdp.ecommerce.controller;


import com.pdp.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/random")
    public HttpEntity<?> getRandomProduct() {
        return ResponseEntity.ok(productService.getRandomProducts());
    }
//
//    @PostMapping("/search")
//    public HttpEntity<?> findProduct(@RequestBody SearchDto searchDto) {
//        return ResponseEntity.ok(productService.findByNameAndGender(searchDto));
//    }
}
