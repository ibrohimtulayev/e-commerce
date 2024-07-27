package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.OrderProduct;
import com.pdp.ecommerce.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {
    private final OrderProductRepository orderProductRepository;
    @Override
    public void save(OrderProduct orderProduct) {
        orderProductRepository.save(orderProduct);
    }
}
