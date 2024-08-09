package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.entity.Discount;
import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.ProductDetails;
import com.pdp.ecommerce.model.dto.DiscountDto;
import com.pdp.ecommerce.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService{
    private final DiscountRepository discountRepository;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductDetailsService productDetailsService;
    private final ObjectMapper mapper;

    @Override
    public void save(Discount discount) {
        discountRepository.save(discount);
    }
    @Override
    public void makeWeeklyDiscount() {
        Random random = new Random();
        int discountAmount = random.nextInt(30, 51);
        List<Category> categories = categoryService.getVeryChildCategories();
        int discountProductsCount = 5;
        List<Category> discountCategories = new ArrayList<>();
        List<Product> discountProducts = new ArrayList<>();
        while (discountCategories.size()!=2){
            Category randomCategory = categories.get(random.nextInt(categories.size()));
            if(!discountCategories.contains(randomCategory)) {
                List<Product> products = productService.getRandomProductsByCategoryId(randomCategory.getId(), discountProductsCount);
                discountCategories.add(randomCategory);
                discountProducts.addAll(products);
            }
        }
        Discount discount = Discount.builder()
                .amount(discountAmount)
                .description("Black Friday")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(24))
                .categories(discountCategories)
                .products(discountProducts)
                .build();
        save(discount);
    }

    @Override
    public HttpEntity<?> getDiscountEvent() {
        List<Discount> discounts = discountRepository.findAll();
        return ResponseEntity.ok(discounts);
    }

    @Override
    public Discount findById(UUID id) {
       return discountRepository.findById(id).orElse(null);
    }

    @Override
    public boolean isValid(Discount discount, ProductDetails productDetails) {
       Product product = productDetailsService.findProduct(productDetails.getId());
       if (discount.getEndDate().isBefore(LocalDateTime.now())&& discount.getProducts().contains(product)) {
           return true;
       }
       return false;
    }

    @Override
    public HttpEntity<?> createDiscount(String imageUrl, DiscountDto discountDto) throws JsonProcessingException {
        List<Discount> discounts = discountRepository.findAll();
        for (Discount discount : discounts) {
            if(discount.getEndDate().isAfter(LocalDateTime.now())){
                discount.setEndDate(LocalDateTime.now());
                discountRepository.save(discount);
            }
        }
        String json = discountDto.selectedProductIds();
        List<UUID> productIds = mapper.readValue(json, new TypeReference<List<UUID>>() {});
        List<Product> products = productIds.stream().map(productService::findById).toList();
        save(Discount.builder()
                .image(imageUrl)
                .startDate(discountDto.startDate())
                .endDate(discountDto.endDate())
                .description(discountDto.description())
                .amount(discountDto.amount())
                .products(products)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    @Override
    public HttpEntity<?> findCurrentDiscount() {
        Discount discount = discountRepository.findCurrentDiscount();
        return ResponseEntity.ok(discount);
    }
}
