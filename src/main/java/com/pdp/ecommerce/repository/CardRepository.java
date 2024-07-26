package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}