package com.pdp.ecommerce.model.dto;

import com.pdp.ecommerce.entity.enums.GenderEnum;

public record SearchDto(String keyword, GenderEnum genderEnum) {
}
