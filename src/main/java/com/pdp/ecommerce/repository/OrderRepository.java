package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}