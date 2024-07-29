package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.model.dto.SearchDto;
import com.pdp.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;


    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getRandomProducts() {
        int amount = 3;
        return productRepository.getRandomProducts(amount);
    }

    @Override
    public List<Product> findByNameAndGender(SearchDto searchDto) {
        return productRepository.findByNameAndGender(searchDto.keyword(), searchDto.gender());
    }

}
