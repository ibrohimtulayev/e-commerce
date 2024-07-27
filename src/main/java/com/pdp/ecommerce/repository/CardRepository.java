package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}