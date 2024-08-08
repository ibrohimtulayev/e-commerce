package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.ProductDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductDetailsService {
    ProductDetails save(ProductDetails productDetails);

    List<ProductDetails> saveAll(List<ProductDetails> productDetailsList);

    Product findProduct(UUID productDetailsId);

    Optional<ProductDetails> findById(UUID productDetailId);
}
