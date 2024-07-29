package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query(nativeQuery = true,value = "select * from category order by random() limit :amount")
    List<Category> getRandomCategories(@Param("amount") int amount);
}