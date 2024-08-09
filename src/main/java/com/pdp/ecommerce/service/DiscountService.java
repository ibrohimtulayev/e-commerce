package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.entity.Discount;
import com.pdp.ecommerce.entity.ProductDetails;
import com.pdp.ecommerce.model.dto.DiscountDto;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DiscountService {
    void save(Discount discount);

    void makeWeeklyDiscount();

    HttpEntity<?> getDiscountEvent();
    Discount findById(UUID id);

    boolean isValid(Discount discount, ProductDetails productDetails);

    HttpEntity<?> createDiscount(String imageUrl, DiscountDto discountDto) throws JsonProcessingException;

    HttpEntity<?> findCurrentDiscount();
}
