package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.model.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
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

    void   updateProductImage(UUID productId, String imageUrl);

    Page<Product> getPagedProductsByCategory(int page, String categoryName);


    HttpEntity<?> getDetailedProductById(UUID id) throws JsonProcessingException;
}
