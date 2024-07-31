package com.pdp.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final DiscountService discountService;
    private final CategoryService categoryService;
    private final ProductService productService;


    @Scheduled(cron = "0 0 0 * * 5")
    public void performTask() {
        discountService.makeWeeklyDiscount();
        System.out.println("Discount is created " + LocalDateTime.now());
        System.out.println("Thread name: " + Thread.currentThread().getName());
    }

}
