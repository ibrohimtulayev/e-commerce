package com.pdp.ecommerce.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

public record ProductCreateDto(
        String productName,
        String productDescription,
        UUID categoryId,
        String details
) {
    /**
     * DTO for {@link com.pdp.ecommerce.entity.Category}
     */
    public static record CategoryDto(@NotBlank(message = "Invalid name") String name,
                                     UUID parentCategoryId) implements Serializable {
    }
}
