package com.pdp.ecommerce.model.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record OrderDto(List<UUID> basketProductsId, Double orderPrice, String cardNumber,
                       String expiryDate, Integer cvv) {
}
