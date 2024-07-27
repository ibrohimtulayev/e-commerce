package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Order;
import com.pdp.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }
}
