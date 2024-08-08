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
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private Double latitude;
    private Double longitude;

    public Address(Double lat, Double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }
}