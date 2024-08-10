package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.model.dto.ProductCreateDto;
import com.pdp.ecommerce.model.dto.SearchDto;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service

public interface ProductService {
    Product save(Product product);

    List<Product> getRandomProductsByCategoryId(UUID id, Integer count);

    HttpEntity<?> findByNameAndGender(SearchDto searchDto);

    HttpEntity<?> recommendProducts();

    Product findOneFavouriteProductByUserId(UUID userId);

    HttpEntity<?> getPagedProductsByCategory(int page, String categoryName);

    Product findById(UUID productId);

    HttpEntity<?> filterBy(UUID categoryId, String filterBy);


    HttpEntity<?> getDetailedProductById(UUID id) ;


    HttpEntity<?> findAllWithCategory();

    HttpEntity<?> getRatingAndReviews(UUID productId) throws JsonProcessingException;

    HttpEntity<?> getProductDescription(UUID productId);

    HttpEntity<?> createProduct(ProductCreateDto productCreateDto, String imageUrl) throws JsonProcessingException;
}
