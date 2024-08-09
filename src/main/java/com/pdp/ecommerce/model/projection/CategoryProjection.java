package com.pdp.ecommerce.model.projection;

import java.util.UUID;

public interface CategoryProjection {
    UUID getParentId();
    UUID getId();
    String getName();
    String[] getSubcategories();
    String getImage();
    int getProductCount();
}
