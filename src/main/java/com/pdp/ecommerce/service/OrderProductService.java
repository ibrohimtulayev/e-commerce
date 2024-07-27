package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.OrderProduct;
import org.springframework.stereotype.Service;

@Service

public interface OrderProductService {
    void save(OrderProduct orderProduct);
}
