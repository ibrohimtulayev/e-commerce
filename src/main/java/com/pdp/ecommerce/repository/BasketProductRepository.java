package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.BasketProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BasketProductRepository extends JpaRepository<BasketProduct, UUID> {
    void removeById(UUID id);

    @Query(value = """
            select bp.* from basket_product bp
            where bp.user_id =:id
            """, nativeQuery = true)
    List<BasketProduct> userBasketProducts(@Param("id") UUID id);
}