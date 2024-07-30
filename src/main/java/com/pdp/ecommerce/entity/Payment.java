package com.pdp.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @ManyToOne
    private User user;
    private Double amount;
    @CreationTimestamp
    private LocalDate paymentDate;
    @OneToOne
    private Order order;

    @ManyToOne
    private Card card;

}