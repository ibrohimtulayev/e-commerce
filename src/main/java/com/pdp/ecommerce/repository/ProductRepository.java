package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query(value = "SELECT * FROM product LIMIT :amount", nativeQuery = true)
    List<Product> getRandomProducts(@Param("amount") int amount);

    @Query(value = """
        SELECT *
        FROM product
        WHERE category_id = :id
        ORDER BY RANDOM()
        LIMIT :amount
    """, nativeQuery = true)
    List<Product> getRandomProductsByCategoryId(@Param("id") UUID id, Integer amount);
}