package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.ProductDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDetailsServiceImpl implements ProductDetailsService{
    private final ProductDetailsRepository productDetailsRepository;

    @Override
    public ProductDetails save(ProductDetails productDetails) {
        return productDetailsRepository.save(productDetails);
    }

    @Override
    public List<ProductDetails> saveAll(List<ProductDetails> productDetailsList) {
        return productDetailsRepository.saveAll(productDetailsList);
    }

    @Override
    public Product findProduct(UUID productDetailsId) {
        return productDetailsRepository.findProduct(productDetailsId);
    }

    @Override
    public Optional<ProductDetails> findById(UUID productDetailId) {
        return productDetailsRepository.findById(productDetailId);
    }
}
