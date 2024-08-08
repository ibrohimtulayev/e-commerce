package com.pdp.ecommerce.model.projection;

import java.util.UUID;

public interface CategoryProductProjection {
    UUID getProductId();
    String getName();
    String getImage();
    String getCategoryName();

}
