package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.entity.ProductDetails;
import com.pdp.ecommerce.model.projection.ProductDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, UUID> {
    @Query(value = """
            SELECT distinct p.*
            FROM product p
                     JOIN product_product_details ppd ON p.id = ppd.product_id
                     JOIN product_details pd ON ppd.product_details_id = pd.id
            WHERE pd.id = :productDetailsId
            """, nativeQuery = true)
    Product findProduct(@Param("productDetailsId") UUID productDetailsId);

    @Query(value = """
            select pd.*,p.name as productName, d.amount as discountAmount, pd.quantity as productAmount from product_details pd
                      join public.product_product_details ppd on pd.id = ppd.product_details_id
                      join public.product p on p.id = ppd.product_id
                      left join discount_products dp on p.id = dp.products_id
                      left join discount d on dp.discount_id = d.id
            where pd.id =:productDetailsId
            """, nativeQuery = true)
    ProductDetailsProjection findBy(@Param("productDetailsId") UUID productDetailsId);

}