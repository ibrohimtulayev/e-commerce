package com.pdp.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @OneToOne
    private ProductDetails productDetails;
    private Integer amount;
    @ManyToOne
    private Order order;

}