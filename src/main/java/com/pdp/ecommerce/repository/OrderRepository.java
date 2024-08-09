package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Order;
import com.pdp.ecommerce.model.projection.UserOrderProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(UUID id);

    @Query(value = """
        select o.created_at as orderDate, 
               o.delivery_time as deliveryTime, 
               op.amount as amount, 
               p.name as products, 
               pay.amount as paymentAmount, 
               pay.payment_date as paymentDate
        from orders o
        left join order_product op on op.order_id = o.id
        left join product_details pd on op.product_details_id = pd.id
        left join product_product_details ppd on pd.id = ppd.product_details_id
        left join product p on ppd.product_id = p.id
        left join payment pay on o.id = pay.order_id
        where o.user_id = :userId
        """, nativeQuery = true)
    List<UserOrderProjection> findOrderByUser(@Param("userId") UUID userId);

}