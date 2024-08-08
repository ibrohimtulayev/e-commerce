package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Discount;
import com.pdp.ecommerce.entity.ProductDetails;
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

}
