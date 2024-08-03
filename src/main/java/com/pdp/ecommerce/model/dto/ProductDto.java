package com.pdp.ecommerce.model.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.pdp.ecommerce.entity.Product}
 */
@Data
public class ProductDto implements Serializable {
    String name;
    String description;
    String image;
}