package com.pdp.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.model.dto.DiscountDto;
import com.pdp.ecommerce.service.DiscountService;
import com.pdp.ecommerce.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/discount")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;
    private final S3Service s3Service;

    @GetMapping
    public HttpEntity<?> getDiscount(){
        return discountService.getDiscountEvent();
    }

    @PostMapping
    public HttpEntity<?> createDiscountEvent(@RequestParam(required = false) MultipartFile image, DiscountDto discountDto) throws JsonProcessingException {
        String imageUrl = s3Service.uploadFile(image);
        return discountService.createDiscount(imageUrl, discountDto);
    }

    @GetMapping("current")
    public HttpEntity<?> getCurrentDiscount(){
        return discountService.findCurrentDiscount();
    }
}
