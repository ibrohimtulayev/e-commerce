package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.entity.Discount;
import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService{
    private final DiscountRepository discountRepository;
    private final CategoryService categoryService;
    private final ProductService productService;

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
        return null;
    }
}
