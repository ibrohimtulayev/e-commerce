package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getRandomCategories() {
        int amount = 4;
       return categoryRepository.getRandomCategories(amount);
    }
}
