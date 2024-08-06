package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Product;
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

import java.util.UUID;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final S3Service s3Service;

    @GetMapping("/random")
    public HttpEntity<?> getRandomProduct() {
        return productService.getRandomProducts();
    }

    @PostMapping("/search")
    public HttpEntity<?> findProduct(@RequestBody SearchDto searchDto) {
        return  productService.findByNameAndGender(searchDto);
    }

    @GetMapping("/recommendation")
    public HttpEntity<?> getRecommendation() {
        return productService.recommendProducts();
    }

    @PostMapping(value = "/uploadImage", consumes = "multipart/form-data")
    public HttpEntity<?> uploadImageAndCreateProduct(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productId") String productId) {
        String imageUrl = s3Service.uploadFile(file);
       return productService.updateProductImage(UUID.fromString(productId), imageUrl);
    }

    @GetMapping("/getPagedProductsByCategory")
    public HttpEntity<?> getPagedProductsByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String categoryName) {
        return productService.getPagedProductsByCategory(page, categoryName);
    }

    // newly_added , best_rating TODO: should add more filters
    @GetMapping("/filter")
    public HttpEntity<?> getFilteredProducts(@RequestParam UUID categoryId,@RequestParam String filterBy){
        return productService.filterBy(categoryId,filterBy);
    }

}
