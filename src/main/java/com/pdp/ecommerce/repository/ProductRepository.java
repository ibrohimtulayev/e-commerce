package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query(value = "SELECT * FROM product ORDER BY RANDOM() LIMIT :amount", nativeQuery = true)
    List<Product> getRandomProducts(@Param("amount") int amount);


    @Query("SELECT p FROM Product p WHERE p.gender = :gender AND FUNCTION('similarity', p.name, :keyword) > 0.2")
    List<Product> findByNameAndGender(@Param("keyword") String keyword, @Param("gender") Gender gender);
}