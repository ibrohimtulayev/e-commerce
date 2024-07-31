package com.pdp.ecommerce.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, UUID> {
}