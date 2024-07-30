package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.repository.UserRepository;
import com.pdp.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/deleteAccount")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;

    @DeleteMapping("yes")
    public HttpEntity<?> deleteAccount(@RequestParam String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            userRepository.delete(byEmail.get());
            return ResponseEntity.ok().body("Account deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
