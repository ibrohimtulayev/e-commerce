package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Order;
import com.pdp.ecommerce.model.dto.OrderDto;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service

public interface OrderService {
    void save(Order order);

    HttpEntity<?> deliveryDestination();

    HttpEntity<?> make(OrderDto orderDto);


    HttpEntity<?> findByUserOrders(UUID id);
}
