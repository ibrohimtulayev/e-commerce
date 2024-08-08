package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    UserDetails findByEmail(String username);

    HttpEntity<?> register(UserRegisterDto userRegisterDto);

    HttpEntity<?> login(UserLoginDto userLoginDto);

    HttpEntity<?> checkVerificationCode(String code, String header) throws JsonProcessingException, BadRequestException;

    User save(User user);

    List<User> findAllUsersByRole(String role);

    Optional<User> getSignedUser();

    HttpEntity<?> getUserSearchHistory();

    void updateUserSearchHistory(String keyword);

    HttpEntity<?> getWishlist();

    HttpEntity<?> addToWishlist(UUID productId);

    HttpEntity<?> clearWishlist();

    HttpEntity<?> removeFavouriteProduct(UUID id);

    HttpEntity<?> changeAddress(Double lat, Double lon);

    HttpEntity<?> getOrders();
}