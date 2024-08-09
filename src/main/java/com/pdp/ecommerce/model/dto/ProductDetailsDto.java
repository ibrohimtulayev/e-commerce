package com.pdp.ecommerce.model.dto;

public record ProductDetailsDto(
        String size,
        String color,
        Double price,
        String gender,
        Integer quantity
) {}
