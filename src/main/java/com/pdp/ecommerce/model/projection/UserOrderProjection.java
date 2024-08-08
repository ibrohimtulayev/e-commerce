package com.pdp.ecommerce.model.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserOrderProjection {
     String productName();
     Double amount();
     String color();
     LocalDateTime orderDate();

}
