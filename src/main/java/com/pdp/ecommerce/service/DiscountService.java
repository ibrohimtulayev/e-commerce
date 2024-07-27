package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Discount;
import org.springframework.stereotype.Service;

@Service

public interface DiscountService {
    void save(Discount discount);
}
