package com.pdp.ecommerce.model.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record OrderDto(List<Map<Integer,UUID>> productDetailIdWithAmount, Double orderPrice, String cardNUmber,
                       String expiryDate, Integer cvv) {

}
