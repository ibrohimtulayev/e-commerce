package com.pdp.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    UserDetails findByEmail(String username);

    HttpEntity<?> register(UserRegisterDto userRegisterDto);

    HttpEntity<?> login(UserLoginDto userLoginDto);

    HttpEntity<?> checkVerificationCode(String code, String header) throws JsonProcessingException, BadRequestException;

    User save(User user);

    List<User> findAllUsersByRole(String role);

    Optional<User> getSignedUser();
    List<String> getUserSearchHistory();
    void updateUserSearchHistory(String keyword);

    List<Product> getWishlist();
}
