package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Discount;
import com.pdp.ecommerce.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class DiscountServiceImpl implements DiscountService{
    private final DiscountRepository discountRepository;
    @Override
    public void save(Discount discount) {
        discountRepository.save(discount);
    }
}
