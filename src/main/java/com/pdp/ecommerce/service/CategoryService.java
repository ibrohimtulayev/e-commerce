package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Category;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CategoryService {
    Category save(Category category);

    List<Category> getRandomCategories();

    Category findById(UUID categoryId);

    List<Category> getVeryChildCategories();

    HttpEntity<?> getAllCategories();
}
