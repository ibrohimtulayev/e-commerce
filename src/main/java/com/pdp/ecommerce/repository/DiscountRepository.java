package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
}