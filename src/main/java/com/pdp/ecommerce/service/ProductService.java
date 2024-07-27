package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import org.springframework.stereotype.Service;

@Service

public interface ProductService {
    void save(Product product);
}
