package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.ProductDetails;
import com.pdp.ecommerce.entity.ProductDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
