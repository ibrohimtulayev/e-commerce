package com.pdp.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer amount;
    private String description;
    @OneToMany
    private List<Category> categories;
    @OneToMany
    private List<Product> products;

}