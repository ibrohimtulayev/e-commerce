package com.pdp.ecommerce.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendConfirmationCode(Integer code, String email);
}
