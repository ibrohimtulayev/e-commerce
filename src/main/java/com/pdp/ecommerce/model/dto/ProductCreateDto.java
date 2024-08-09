package com.pdp.ecommerce.model.dto;

import java.util.UUID;

public record ProductCreateDto(
        String productName,
        String productDescription,
        UUID categoryId,
        String details
) { }
