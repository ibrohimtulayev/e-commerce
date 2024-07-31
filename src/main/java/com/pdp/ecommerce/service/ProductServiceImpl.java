package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getRandomProducts() {
        int amount = 3;
        return productRepository.getRandomProducts(amount);
    }

    @Override
    public List<Product> getRandomProductsByCategoryId(UUID id, Integer amount) {
        return productRepository.getRandomProductsByCategoryId(id, amount);
    }
}
