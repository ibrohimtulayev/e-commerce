package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
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
}
