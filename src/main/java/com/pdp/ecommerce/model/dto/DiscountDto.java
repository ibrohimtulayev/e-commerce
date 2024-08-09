package com.pdp.ecommerce.model.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.pdp.ecommerce.entity.Discount}
 */
public record DiscountDto(@NotNull LocalDateTime startDate, @NotNull LocalDateTime endDate, @NotNull Integer amount,
                          @NotNull String description, String selectedProductIds) implements Serializable {
}