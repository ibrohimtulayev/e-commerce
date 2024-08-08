package com.pdp.ecommerce.model.projection;

import java.util.UUID;

public interface ProductDetailsProjection {

    String getGender();
    String getSize();
    String getColor();
    Integer getAmount();
    Double getPrice();
    Double getDiscountAmount();
    String getProductName();
}
