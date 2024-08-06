package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/searchHistory")
    public HttpEntity<?> getSearchHistory(){
        return ResponseEntity.ok(userService.getUserSearchHistory());
    }

    @GetMapping("/wishlist")
    public HttpEntity<?> getUserWishlist(){
      return ResponseEntity.ok(null);
    }
}

