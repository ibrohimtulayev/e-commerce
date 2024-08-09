package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.model.dto.ProductCreateDto;
import com.pdp.ecommerce.service.CategoryService;
import com.pdp.ecommerce.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final S3Service s3Service;


    @GetMapping("/random")
    public HttpEntity<?> getRandomCategories() {
        return ResponseEntity.ok(categoryService.getRandomCategories());
    }

    @GetMapping("/all")
    public HttpEntity<?> getParentCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("subcategories")
    public HttpEntity<?> getSubCategories() {
        return categoryService.getAllSubcategories();
    }

    @PostMapping
    public HttpEntity<?> createCategory(@RequestParam(required = false) MultipartFile image, ProductCreateDto.CategoryDto categoryDto) {
        String imageUrl = s3Service.uploadFile(image);
        return categoryService.createCategory(imageUrl, categoryDto);
    }

}
