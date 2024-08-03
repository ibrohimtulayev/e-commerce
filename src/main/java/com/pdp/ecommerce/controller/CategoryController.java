package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping("/random")
    public HttpEntity<?> getRandomCategories() {
        return ResponseEntity.ok(categoryService.getRandomCategories());
    }

    @GetMapping("/all")
    public HttpEntity<?> getParentCategories() {
        return categoryService.getAllCategories();
    }
}
