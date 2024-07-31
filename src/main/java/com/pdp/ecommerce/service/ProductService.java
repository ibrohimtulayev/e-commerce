package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.model.dto.SearchDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service

public interface ProductService {
    Product save(Product product);

    List<Product> getRandomProducts();

    List<Product> getRandomProductsByCategoryId(UUID id, Integer count);

    List<Product> findByNameAndGender(SearchDto searchDto);

    List<Product> recommendProducts();

    Product findOneFavouriteProductByUserId(UUID userId);
}
