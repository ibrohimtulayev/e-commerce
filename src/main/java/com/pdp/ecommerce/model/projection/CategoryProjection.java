package com.pdp.ecommerce.model.projection;

import java.util.UUID;

public interface CategoryProjection {
    UUID getCategoryId();
    String getCategoryName();
    String[] getSubCategories();
    int getProductCount();
}
