package com.pdp.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;


    @Override
    public void sendConfirmationCode(Integer code, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@example.com");
        message.setTo(email);
        message.setSubject("Verification Code");
        message.setText("Your verification code for ecommerce application is : " + code);

        mailSender.send(message);
    }
}
