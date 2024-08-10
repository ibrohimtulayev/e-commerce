package com.pdp.ecommerce.model.projection;

import java.util.UUID;

public interface ProductDetailsProjection {

    String getGender();
    UUID getBasketProductId();
    String getSize();
    String getColor();
    Integer getProductAmount();
    Double getPrice();
    Double getDiscountAmount();
    String getProductName();
}
