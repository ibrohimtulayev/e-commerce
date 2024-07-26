package com.pdp.ecommerce.model;

import com.pdp.ecommerce.model.enums.OrderStatus;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime deliveryTime;
    @CreationTimestamp
    private LocalDateTime createdAt;



}