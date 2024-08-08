package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    @Query("""
            select c from Card c
            where c.cardNumber =:cardNumber
            """)
    Card findByNumber(@Param("cardNumber") String cardNUmber);

}