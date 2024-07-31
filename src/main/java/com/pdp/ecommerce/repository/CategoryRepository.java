package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query(nativeQuery = true, value = "select * from category where category.parent_category_id IS NULL order by random() limit :amount")
    List<Category> getRandomCategories(@Param("amount") int amount);

    @Query(value = """
            SELECT DISTINCT c.* FROM category c 
            JOIN product p on c.id = p.category_id 
            """, nativeQuery = true)
    List<Category> getVeryChildCategories();
}