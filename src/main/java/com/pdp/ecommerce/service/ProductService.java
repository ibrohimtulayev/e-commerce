package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface ProductService {
    void save(Product product);

    List<Product> getRandomProducts();


}
