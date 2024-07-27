package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import com.pdp.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;
    @PostMapping("register")
    public HttpEntity<?> registration(@RequestBody UserRegisterDto userRegisterDto){
        return userService.register(userRegisterDto);
    }

    @PostMapping("login")
    public HttpEntity<?> login(@RequestBody UserLoginDto userLoginDto){
        return userService.login(userLoginDto);
    }
}
