package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.model.dto.SearchDto;
import com.pdp.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getRandomProducts() {
        int amount = 3;
        return productRepository.getRandomProducts(amount);
    }

    @Override
    public List<Product> getRandomProductsByCategoryId(UUID id, Integer amount) {
        return productRepository.getRandomProductsByCategoryId(id, amount);
    }

    @Override
    public List<Product> findByNameAndGender(SearchDto searchDto) {
        String keyword = searchDto.keyword();
        String gender = searchDto.genderEnum().name();
        userService.updateUserSearchHistory(keyword);
        return productRepository.findByNameAndGender(keyword, gender);
    }

    @Override
    public List<Product> recommendProducts() {
        Optional<User> user = userService.getSignedUser();

        List<Product>getProducts = new ArrayList<>();
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
        return getProducts;
    }

    @Override
    public Product findOneFavouriteProductByUserId(UUID id) {
        return productRepository.findOneFavouriteProductByUserId(id)
                .orElse(null);
    }

    @Override
    public void updateProductImage(UUID productId, String imageUrl) {
        Optional<Product> byId = productRepository.findById(productId);
        if (byId.isPresent()) {
            Product product = byId.get();
            product.setImage(imageUrl);
            productRepository.save(product);
        }else {
            throw new RuntimeException("Product not found");
        }
    }

}