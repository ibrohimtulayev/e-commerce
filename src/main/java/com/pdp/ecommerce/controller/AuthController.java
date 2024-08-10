package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.model.dto.ConfirmationCodeDto;
import com.pdp.ecommerce.model.dto.TokenDto;
import com.pdp.ecommerce.model.dto.UserLoginDto;
import com.pdp.ecommerce.model.dto.UserRegisterDto;
import com.pdp.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "This endpoint saves provided information. Returns a confirmation token and send confirmation code to email if successful. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confirmation code successfully send,",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "409", description = "User already registered with the provided email",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("register")
    public HttpEntity<?> registration(@RequestBody @Valid UserRegisterDto userRegisterDto){
        return userService.register(userRegisterDto);
    }

    @Operation(
            summary = "Confirm confirmation code",
            description = "This endpoint checks the user-entered code with the email code. If successful, the user will be registered. The `Confirmation` header must be provided to verify the request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid confirmation code or header",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("confirmation")
    public HttpEntity<?> confirmation(@RequestBody ConfirmationCodeDto confirmationCodeDto,
                                      @RequestHeader("Confirmation") String header) {
        return userService.checkVerificationCode(confirmationCodeDto.code(), header);
    }

    @Operation(summary = "Login a user",
            description = "This endpoint allows users to authenticate by providing their email and password. Upon successful authentication, a JWT token and a refresh token are generated and returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful. Returns a JWT token and refresh token.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Account is disabled or locked.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("login")
    public HttpEntity<?> login(@RequestBody UserLoginDto userLoginDto){
        return userService.login(userLoginDto);
    }
}
