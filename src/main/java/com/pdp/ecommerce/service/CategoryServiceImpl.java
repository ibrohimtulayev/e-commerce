package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.model.dto.ProductCreateDto;
import com.pdp.ecommerce.model.projection.CategoryProjection;
import com.pdp.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getRandomCategories() {
        int amount = 4;
        return categoryRepository.getRandomCategories(amount);
    }

    @Override
    public Category findById(UUID categoryId) {
        return categoryRepository.findById(categoryId).get();
    }


    @Override
    public List<Category> getVeryChildCategories() {
        return categoryRepository.getVeryChildCategories();
    }

    @Override
    public HttpEntity<?> getAllCategories() {
        List<CategoryProjection> categories = categoryRepository.findAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Override
    public HttpEntity<?> getAllSubcategories() {
        List<Category> subcategories = categoryRepository.findAllByParentCategoryIdIsNotNull();
        return ResponseEntity.ok(subcategories);
    }

    @Override
    public HttpEntity<?> createCategory(String imageUrl, ProductCreateDto.CategoryDto categoryDto) {
        categoryRepository.save(Category.builder()
                .name(categoryDto.name())
                .image(imageUrl)
                .parentCategoryId(categoryDto.parentCategoryId())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body("Category successfully created!");
    }
}
