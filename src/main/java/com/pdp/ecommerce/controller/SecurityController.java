package com.pdp.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pdp.ecommerce.model.dto.ConfirmationCodeDto;
import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import com.pdp.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;
    @PostMapping("register")
    public HttpEntity<?> registration(@RequestBody UserRegisterDto userRegisterDto){
        return userService.register(userRegisterDto);
    }

    @Operation(summary = "Example endpoint", description = "An endpoint to demonstrate custom headers")
    @PostMapping("confirmation")
    public HttpEntity<?> confirmation(@RequestBody ConfirmationCodeDto confirmationCodeDto, @RequestHeader("Confirmation") String header) throws BadRequestException, JsonProcessingException {
        return userService.checkVerificationCode(confirmationCodeDto.code(), header);
    }

    @PostMapping("login")
    public HttpEntity<?> login(@RequestBody UserLoginDto userLoginDto){
        return userService.login(userLoginDto);
    }
}
