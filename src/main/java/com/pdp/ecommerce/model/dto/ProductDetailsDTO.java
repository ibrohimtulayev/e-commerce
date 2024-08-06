package com.pdp.ecommerce.model.dto;

import java.util.UUID;

public record ProductDetailsDTO(
        UUID id,
        String gender,
        String size,
        String color,
        Integer quantity,
        Double price
) {}
