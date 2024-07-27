package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Payment;
import com.pdp.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }
}
