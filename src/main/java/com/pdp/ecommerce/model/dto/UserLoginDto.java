package com.pdp.ecommerce.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link com.pdp.ecommerce.entity.User}
 */
public record UserLoginDto(
        @Email(message = "Please provide valid email format") @NotBlank(message = "Email must not be null, not empty, and not contain only whitespace.") String email,
        @NotBlank(message = "Password must not be null, not empty, and not contain only whitespace.") String password
) implements Serializable {
}