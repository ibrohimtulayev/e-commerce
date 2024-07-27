package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}