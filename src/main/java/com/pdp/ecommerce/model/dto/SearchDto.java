package com.pdp.ecommerce.model.dto;

import com.pdp.ecommerce.entity.enums.Gender;

public record SearchDto(String keyword, Gender gender) {
}
