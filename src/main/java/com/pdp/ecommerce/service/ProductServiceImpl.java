package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.ProductDetails;
import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.entity.enums.GenderEnum;
import com.pdp.ecommerce.model.dto.ProductCreateDto;
import com.pdp.ecommerce.model.dto.ProductDetailsDto;
import com.pdp.ecommerce.model.dto.SearchDto;
import com.pdp.ecommerce.model.projection.CategoryProductProjection;
import com.pdp.ecommerce.model.projection.ProductProjection;
import com.pdp.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final CategoryService categoryService;
    private final ProductDetailsRepository productDetailsRepository;


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getRandomProductsByCategoryId(UUID id, Integer amount) {
        return productRepository.getRandomProductsByCategoryId(id, amount);
    }

    @Override
    public HttpEntity<?> findByNameAndGender(SearchDto searchDto) {
        String keyword = searchDto.keyword();
        String gender = searchDto.genderEnum().name();
        userService.updateUserSearchHistory(keyword);
        return ResponseEntity.ok(productRepository.findByNameAndGender(keyword, gender));
    }

    @Override
    public HttpEntity<?> recommendProducts() {
        Optional<User> user = userService.getSignedUser();

        List<Product> getProducts = new ArrayList<>();
        if (user.isPresent()) {
            User signedUser = user.get();
            if (signedUser.getFavouriteProducts() != null && !signedUser.getFavouriteProducts().isEmpty()) {
                Product product = findOneFavouriteProductByUserId(signedUser.getId());
                System.out.println(product.getName());
                getProducts.add(product);
                getProducts = productRepository.getProductsWithFavouriteType(product.getCategory().getId(), product.getId());
            } else {
                getProducts = productRepository.getRandomProducts(4);
            }
        }
        return ResponseEntity.ok(getProducts);
    }

    @Override
    public Product findOneFavouriteProductByUserId(UUID id) {
        return productRepository.findOneFavouriteProductByUserId(id)
                .orElse(null);
    }

    @Override
    public HttpEntity<?> getPagedProductsByCategory(int page, String categoryName) {
        if(page <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Page must be greater than 0");
        }
        page = page -1;
        Pageable pageable = PageRequest.of(page, 10); // 10 items per page
        return ResponseEntity.ok(productRepository.getPagedProductsByCategoryName(categoryName, pageable));
    }

    @Override
    public Product findById(UUID productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public HttpEntity<?> filterBy(UUID categoryId, String filterBy) {
        List<ProductProjection> filteredProducts = switch (filterBy) {
            case "newly_added" -> productRepository.filterByNewlyAdded(categoryId);
            case "best_rating" -> productRepository.filterByRating(categoryId);
            default -> productRepository.findByCategoryId(categoryId);
        };
        return ResponseEntity.ok(filteredProducts);
    }


    @Override
    @SneakyThrows
    public HttpEntity<?> getDetailedProductById(UUID id)  {
        String detailedProduct = productRepository.findDetailedProductById(id);
        if(detailedProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
        }
        return ResponseEntity.ok(objectMapper.readTree(detailedProduct));
    }

    @Override
    public HttpEntity<?> findAllWithCategory() {
        List<CategoryProductProjection> all = productRepository.findAllWithCategory();
        return ResponseEntity.ok(all);
    }


    @Override
    @SneakyThrows
    public HttpEntity<?> getRatingAndReviews(UUID productId){
        Optional<Product> byId = productRepository.findById(productId);
        if(byId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
        }
        List<String> ratingAndReviews = productRepository.findRatingAndReviewsByProductId(productId);
        List<JsonNode> jsonNodes = new ArrayList<>();
        for (String ratingAndReview : ratingAndReviews) {
            jsonNodes.add(objectMapper.readTree(ratingAndReview));
        }
        return ResponseEntity.ok(jsonNodes);
    }

    @Override
    public HttpEntity<?> getProductDescription(UUID productId) {
        Optional<Product> byId = productRepository.findById(productId);
        if(byId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        String description = productRepository.findDescriptionById(productId);
        return ResponseEntity.ok(description);
    }

    @Override
    @SneakyThrows
    public HttpEntity<?> createProduct(ProductCreateDto productDto, String imageUrl) {
        ObjectMapper mapper = new ObjectMapper();
        List<ProductDetailsDto> prodDetailsDto = mapper.readValue(productDto.details(), new TypeReference<List<ProductDetailsDto>>() {
        });
        List<ProductDetails> prodDetails = prodDetailsDto.stream().map(item -> ProductDetails.builder()
                .quantity(item.quantity())
                .size(item.size())
                .color(item.color())
                .price(item.price())
                .gender(GenderEnum.valueOf(item.gender()))
                .build()).toList();
        List<ProductDetails> productDetails = productDetailsRepository.saveAll(prodDetails);

        productRepository.save(Product.builder()
                .category(categoryService.findById(productDto.categoryId()))
                .image(imageUrl)
                .name(productDto.productName())
                .description(productDto.productDescription())
                .productDetails(productDetails)
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body("Product successfully created.");
    }
}