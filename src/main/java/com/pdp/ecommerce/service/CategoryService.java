package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CategoryService {
    void save(Category category);

    List<Category> getRandomCategories();

    Category findById(UUID categoryId);



}
