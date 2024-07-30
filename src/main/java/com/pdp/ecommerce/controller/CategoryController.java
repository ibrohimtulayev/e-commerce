package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping("/random")
    public HttpEntity<?> getRandomCategories() {
        return ResponseEntity.ok(categoryService.getRandomCategories());
    }
}
