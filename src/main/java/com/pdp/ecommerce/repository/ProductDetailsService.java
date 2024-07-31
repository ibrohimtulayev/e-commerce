package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.ProductDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductDetailsService {
    ProductDetails save(ProductDetails productDetails);

    List<ProductDetails> saveAll(List<ProductDetails> productDetailsList);
}
