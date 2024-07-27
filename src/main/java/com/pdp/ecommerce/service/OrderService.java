package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Order;
import org.springframework.stereotype.Service;

@Service

public interface OrderService {
    void save(Order order);
}
