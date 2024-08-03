package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.model.dto.ProductDto;
import com.pdp.ecommerce.model.dto.SearchDto;
import com.pdp.ecommerce.service.ProductService;
import com.pdp.ecommerce.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final S3Service s3Service;

    @GetMapping("/random")
    public HttpEntity<?> getRandomProduct() {
        return ResponseEntity.ok(productService.getRandomProducts());
    }

    @PostMapping("/search")
    public HttpEntity<?> findProduct(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(productService.findByNameAndGender(searchDto));
    }

    @GetMapping("/recommendation")
    public HttpEntity<?> getRecommendation() {
        return ResponseEntity.ok(productService.recommendProducts());
    }

    @PostMapping(value = "/uploadImage", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadImageAndCreateProduct(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productId") String productId) {

        String imageUrl = s3Service.uploadFile(file);
        System.out.println(imageUrl);
        productService.updateProductImage(UUID.fromString(productId), imageUrl);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @GetMapping("/getPagedProductsByCategory")
    public Page<Product> getPagedProductsByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam UUID categoryId) {
        return productService.getPagedProductsByCategory(page, categoryId);
    }

}