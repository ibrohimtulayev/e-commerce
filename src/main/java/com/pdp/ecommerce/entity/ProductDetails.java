package com.pdp.ecommerce.entity;

import com.pdp.ecommerce.entity.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_details")

public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String size;
    private String color;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private Integer quantity;
    private Double price;
}