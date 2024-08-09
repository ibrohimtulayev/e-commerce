package com.pdp.ecommerce.model.projection;

import org.apache.catalina.LifecycleState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDateTime;


public interface UserOrderProjection {
     List<String> getProducts();
     Double getAmount();
     LocalDate getDeliveryTime();
     LocalDate getPaymentDate();
     Double getPaymentAmount();
}
