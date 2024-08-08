package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.BasketProduct;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BasketProductService {
    HttpEntity<?> add(UUID productDetailsId, Integer amount);

    HttpEntity<?> remove(UUID id);


    List<BasketProduct> userBasketProduct();
}
