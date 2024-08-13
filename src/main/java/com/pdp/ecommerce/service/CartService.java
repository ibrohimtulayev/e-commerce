package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.BasketProduct;
import com.pdp.ecommerce.entity.ProductDetails;
import com.pdp.ecommerce.model.projection.ProductDetailsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final BasketProductService basketProductService;
    private final ProductDetailsRepository productDetailsRepository;


    public HttpEntity<?> showAll() {
        List<BasketProduct> basketProductList = basketProductService.userBasketProduct();
        List<ProductDetailsProjection> productDetailsProjection = new ArrayList<>();
        for (BasketProduct basketProduct : basketProductList) {
            ProductDetails productDetails = basketProduct.getProductDetails();
            ProductDetailsProjection projection = productDetailsRepository.findBy(productDetails.getId());
            productDetailsProjection.add(projection);
        }
        return ResponseEntity.ok(productDetailsProjection);
    }
}
