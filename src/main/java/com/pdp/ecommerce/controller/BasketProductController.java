package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.service.BasketProductService;
import com.pdp.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/basket-product")
public class BasketProductController {
    private final BasketProductService basketProductService;


    @PostMapping("/add")
    public HttpEntity<?> addBasketProduct(@RequestParam UUID productDetailsId, @RequestParam Integer amount) {
        return basketProductService.add(productDetailsId,amount);
    }

    @GetMapping("/remove")
    public HttpEntity<?> getAllBasketProducts(@RequestParam UUID id) {
        return basketProductService.remove(id);

    }


}
