package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/searchHistory")
    public HttpEntity<?> getSearchHistory(){
        return userService.getUserSearchHistory();
    }

    @GetMapping("/wishlist")
    public HttpEntity<?> getUserWishlist(){
      return userService.getWishlist();
    }
    @PostMapping("wishlist/add")
    public HttpEntity<?> addToWishlist(@RequestParam UUID productId){
        return userService.addToWishlist(productId);
    }
    @GetMapping("wishlist/clear")
    public HttpEntity<?> clearWishlist(){
      return userService.clearWishlist();
    }

    @DeleteMapping("wishlist/{productId}")
    public HttpEntity<?> removeFromWishlist(@PathVariable UUID productId){
        return userService.removeFavouriteProduct(productId);
    }

    @PostMapping("/change-address")
    public HttpEntity<?> changeAddress(@RequestParam Double lat, @RequestParam Double lon){
        return userService.changeAddress(lat,lon);
    }
    @GetMapping("/orders")
    public HttpEntity<?>getUserOrders(){
        return userService.getOrders();
    }
}

