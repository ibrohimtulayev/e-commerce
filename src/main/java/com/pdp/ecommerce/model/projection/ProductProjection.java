package com.pdp.ecommerce.model.projection;

import java.util.List;

public interface ProductProjection {
    String getName();
    String getCategoryName();
    Integer getDiscount();
    Float getAverageRating();
    Integer getRatingCount();
    List<ProductDetailsProjection> getProductDetails();
}
