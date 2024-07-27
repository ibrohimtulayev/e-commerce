package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Payment;
import org.springframework.stereotype.Service;

@Service

public interface PaymentService {
    void save(Payment payment);
}
