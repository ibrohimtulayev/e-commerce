package com.pdp.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String name;
    private String description;
    @OneToOne(cascade = CascadeType.PERSIST)
    private ProductDetails productDetails;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}