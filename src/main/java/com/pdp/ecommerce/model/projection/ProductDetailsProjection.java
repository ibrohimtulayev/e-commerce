package com.pdp.ecommerce.model.projection;

import java.util.UUID;

public interface ProductDetailsProjection {
    UUID getId();
    String getGender();
    String getSize();
    String getColor();
    Integer getQuantity();
    Double getPrice();
}
