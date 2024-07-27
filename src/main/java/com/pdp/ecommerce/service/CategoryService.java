package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    void save(Category category);
}
