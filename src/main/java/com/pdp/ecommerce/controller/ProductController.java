package com.pdp.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.model.dto.ProductCreateDto;
import com.pdp.ecommerce.model.dto.SearchDto;
import com.pdp.ecommerce.service.ProductService;
import com.pdp.ecommerce.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final S3Service s3Service;

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

    @GetMapping("{id}")
    public HttpEntity<?> getProduct(@PathVariable UUID id) throws JsonProcessingException {
        return productService.getDetailedProductById(id);
    }

    @GetMapping("rating-review")
    public HttpEntity<?> getRatingReview(@RequestParam UUID productId) throws JsonProcessingException {
        return productService.getRatingAndReviews(productId);
    }

    @GetMapping("description")
    public HttpEntity<?> getProductDescription(@RequestParam UUID productId) {
        return productService.getProductDescription(productId);
    }

    @GetMapping("findAll")
    public HttpEntity<?> findAllProducts() {
        return productService.findAllWithCategory();
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    public HttpEntity<?> addProduct(@RequestParam(required = false) MultipartFile image, ProductCreateDto productCreateDto) throws JsonProcessingException {
        String imageUrl = s3Service.uploadFile(image);
        return productService.createProduct(productCreateDto, imageUrl);
    }
}
