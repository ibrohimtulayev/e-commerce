package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}