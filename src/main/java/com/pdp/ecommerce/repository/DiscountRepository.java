package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface  DiscountRepository extends JpaRepository<Discount, UUID> {
    @Query(value = """
            select * from discount 
                     where end_date> (CURRENT_TIMESTAMP + INTERVAL '5 hours')
            """, nativeQuery = true)
    Discount findCurrentDiscount();
}