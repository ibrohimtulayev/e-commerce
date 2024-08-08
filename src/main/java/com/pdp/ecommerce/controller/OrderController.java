package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Order;
import com.pdp.ecommerce.model.dto.OrderDto;
import com.pdp.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/delivery")
    public HttpEntity<?> orderPage() {
       return orderService.deliveryDestination();
    }
    @PostMapping("make")
    public HttpEntity<?> makeOrder(@RequestBody OrderDto orderDto) {
        return orderService.make(orderDto);
    }

}
