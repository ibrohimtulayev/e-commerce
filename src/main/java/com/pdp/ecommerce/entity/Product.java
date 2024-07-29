package com.pdp.ecommerce.entity;

import com.pdp.ecommerce.entity.enums.Gender;
import com.pdp.ecommerce.entity.enums.SizeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String name;
    private String description;
    private Double price;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = SizeEnum.class)
    private List<SizeEnum>sizes;

    @ElementCollection
    private List<String>colors;

    @ManyToOne
    private Category category;

    @OneToMany
    private List<Rating>ratings;
    @OneToMany
    private List<Comment>comments;


}