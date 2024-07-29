package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public HttpEntity<?> getRandomCategories(){
       List<Category> categories =categoryService.getRandomCategories();
       return ResponseEntity.ok(categories);
    }
    
}
