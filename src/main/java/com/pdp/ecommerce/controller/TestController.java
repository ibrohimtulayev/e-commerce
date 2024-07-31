package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.entity.enums.RoleEnum;
import com.pdp.ecommerce.repository.CategoryRepository;
import com.pdp.ecommerce.repository.UserRepository;
import com.pdp.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @DeleteMapping("api/deleteAccount/yes")
    public HttpEntity<?> deleteAccount(@RequestParam String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            userRepository.delete(byEmail.get());
            return ResponseEntity.ok().body("Account deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

//    @GetMapping("api/getChildCategories")
//    public HttpEntity<?> getChildCategories() {
//        List<Category> childCategories =  categoryRepository.getVeryChildCategories();
//    }
    @GetMapping("api/getAllUsers")
    public HttpEntity<?> getAllUsers(){
        List<User> users = userRepository.findByRoles(RoleEnum.ROLE_USER.name());
        return ResponseEntity.ok(users);
    }


}
