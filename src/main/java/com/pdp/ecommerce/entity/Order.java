package com.pdp.ecommerce.entity;

import com.pdp.ecommerce.entity.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Change to AUTO or another suitable strategy
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    private LocalDateTime deliveryTime;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
